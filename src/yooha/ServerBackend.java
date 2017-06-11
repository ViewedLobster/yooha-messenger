package yooha;


import yooha.Message;
import yooha.network.Connection;
import yooha.cipher.CipherHandler;
import yooha.cipher.CipherHandlerConstructer;
import yooha.cipher.KeyHandler;
import yooha.network.FileSender;
import yooha.network.FileReceiver;

import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Date;


public class ServerBackend extends ChatBackend implements MessageStringHandler
{

    List<Connection> connections;
    File fileToSend;
    long filesendtimestamp;

    public ServerBackend( int chat_id )
    {
        this.chat_id = chat_id;
        connections = new LinkedList<Connection>();
        this.connectionDatas = new ArrayList<ConnectionData>();
        this.keyHandler = new KeyHandler();
        this.encrypt = false;
    }
    
    public ServerBackend( Connection conn, String nick, int chat_id )
    {
        this(chat_id);
        addConnection(conn, nick);
    }

    public synchronized void sendMessage(Message message){

        CipherHandler ch;
        for ( Connection conn : connections )
        {
            if (this.encrypt)
                ch = getConnData(conn).outCipherHandler;
            else
                ch = null;
            String xmlMessage = MessageDeparser.deparseMessage(message, ch );
            System.out.println("sending message");
            conn.sendString(xmlMessage);
        }
    }

    //public synchronized void addConnection(Connection conn)
    //{
    //    conn.setMessageHandler(this);
    //    connections.add(conn);
    //    connectionDatas.add( new ConnectionData( null, null, conn.connectionId) );
    //}

    public synchronized void addConnection(Connection conn, String nick)
    {
        conn.setMessageHandler(this);
        connections.add(conn);
        connectionDatas.add( new ConnectionData( nick, null, conn.connectionId) );
        String keyType = keyHandler.getDefaultTypeString();
        sendKeyRequest( conn, keyType, keyHandler.getDefaultKeyString( keyType ));
    }

    public void handleMessageString( String messageString, Connection conn )
    {
        // TODO
        // Send the message to all connections except sending one
        // Parse the message
        CipherHandler inCH = getConnData(conn).inCipherHandler;
        Message m = MessageParser.parseString(messageString, inCH);
        if (m != null )
        {
            if ( m.disconnect )
            {
                conn.shutdown();
                removeConnection( conn );
                chat.showMessage(MessageParser.getDisconnectMessage(m.senderName));
            }
            else if( m.keyRequest )
            {
                // TODO
                // check if cipher is supported, if not send reply no
                // construct cipherhandler for incoming for this connection
                // check if it is reply, if not we send a reply
                //
                //
                // If it is reply
                
                // If it is a reply
                if ( m.keyReply != null )
                {
                    // If not supported do not do anything
                    if ( "not_supported".equals( m.keyReply ) )
                    {

                    }
                    else if ( "supported".equals( m.keyReply )  )
                    {   // If the reply sais supported, set cipher handlers
                        ConnectionData cd  = getConnData( conn.connectionId );
                        // Only set cipherhandlers if the sent key request type matches the reply received
                        if ( cd.hasSentKeyRequest(m.keyType) )
                        {
                            cd.setOutCipherHandler( CipherHandlerConstructer.getCipherHandler( m.keyType, m.key, false ));
                            cd.setInCipherHandler( CipherHandlerConstructer.getCipherHandler(m.keyType, cd.keyRequestKey, true) );
                        }
                    }
                }
                else
                {
                    if ( CipherHandler.supportedCipher(m.keyType) )
                    {
                        // We set outgoing cipher handler and get the default key from the keyhandler ( matching the specified cipher type )
                        // We then send this back tho the requester
                        setOutCipherHandler(conn.connectionId, CipherHandlerConstructer.getCipherHandler( m.keyType, m.key, false ));
                        String key = keyHandler.getDefaultKeyString(m.keyType);
                        setInCipherHandler(conn.connectionId, CipherHandlerConstructer.getCipherHandler(m.keyType, key, true));
                        String xmlReply = MessageDeparser.getKeyRequestReplyString( MainView.getNick(), true, m.keyType, key);
                        conn.sendString(xmlReply);
                    }
                    else
                    {
                        // If the cipher is not supported we send reply saying so.
                        String xmlReply = MessageDeparser.getKeyRequestReplyString( MainView.getNick(), false, m.keyType, null );
                        conn.sendString(xmlReply);
                    }
                }

                printCipherHandlers();
                
            }
            else if( m.fileRequest )
            {
                handleFileRequest( m, conn );
            }
            else if( m.fileResponse )
            {
                handleFileResponse ( m, conn );
            }
            else
            {
                updateConndata(m.senderName, conn);
                synchronized ( this )
                {
                    for ( Connection c : connections )
                    {
                        if ( c.connectionId != conn.connectionId )
                        {
                            CipherHandler ch;
                            if ( this.encrypt )
                                ch = getConnData(c).outCipherHandler;
                            else
                                ch = null;
                            String xmlString = MessageDeparser.deparseMessage(m, ch);
                            c.sendString(xmlString);
                        }
                    }
                }
                chat.showMessage(m);
            }
        }
        
    }

    public synchronized void updateConndata(String senderName, Connection conn )
    {
        
        for ( ConnectionData connData : connectionDatas )
        {
            if ( connData.connectionId == conn.connectionId )
            {
                connData.nick = senderName;
                break;
            }
        }
    }

    public synchronized List<ConnectionData> getConnDatas()
    {
        List<ConnectionData> cd = new ArrayList<ConnectionData>();

        for (ConnectionData c : connectionDatas )
        {
            cd.add(c);
        }

        return cd;
    }

    public List<ConnectionData> getConnectionData()
    {
        return getConnDatas();
    }

    public synchronized void removeConnection( Connection conn )
    {
        boolean removed = false;
        for( int i = 0; i < connections.size(); i++ )
        {
            if (connections.get(i).connectionId == conn.connectionId &&
                    connectionDatas.get(i).connectionId == conn.connectionId )
            {
                connections.remove(i);
                connectionDatas.remove(i);
                removed = true;
                break;
            }
        }

        if ( !removed )
        {
            System.err.println("Did not remove connection (connectionId: " + conn.connectionId +")");
        }
    }

    public synchronized void removeAndShutdownConnection( int id )
    {
        boolean removed = false;
        for( int i = 0; i < connections.size(); i++ )
        {
            if (connections.get(i).connectionId == id &&
                    connectionDatas.get(i).connectionId == id )
            {
                Connection conn = connections.remove(i);
                connectionDatas.remove(i);
                removed = true;
                conn.sendString(MessageDeparser.getDisconnectXml("chat_server"));
                conn.shutdown();
                break;
            }
        }

        if ( !removed )
        {
            System.err.println("Did not remove connection (connectionId: " + id +")");
        }
    }


    public void shutdown()
    {
        for ( Connection conn : connections )
        {
            conn.shutdown();
        }
    }

    public void sendDisconnect()
    {
        String xml = MessageDeparser.getDisconnectXml(MainView.getNick());
        for (Connection conn : connections )
            conn.sendString(xml);
    }

    
    public void sendKeyRequestToAll( String type , String keyString )
    {
        String xmlKeyRequest = MessageDeparser.getKeyRequestString( MainView.getNick(), type, keyString );

        synchronized( this )
        {
            Iterator<Connection> connIt = connections.iterator();
            Iterator<ConnectionData> connDataIt = connectionDatas.iterator();

            while ( connIt.hasNext() )
            {
                Connection conn = connIt.next();
                ConnectionData connData = connDataIt.next();

                conn.sendString(xmlKeyRequest);
                connData.setSentKeyRequest( type, keyString );

            }
        }
        
    }

    public synchronized void setInCipherHandler( int connId, CipherHandler ch )
    {
        for (ConnectionData connData: connectionDatas )
        {
            if ( connData.connectionId == connId )
            {
                connData.setInCipherHandler(ch);
                break;
            }
        }
    }

    public synchronized void setOutCipherHandler( int connId, CipherHandler ch )
    {
        for (ConnectionData connData: connectionDatas )
        {
            if ( connData.connectionId == connId )
            {
                connData.setOutCipherHandler(ch);
                break;
            }
        }
    }

    public void sendKeyRequest( Connection conn, String type, String keyString)
    {
        String xmlKeyRequest = MessageDeparser.getKeyRequestString( MainView.getNick(), type, keyString );

        synchronized(this)
        {
            
            for ( ConnectionData connData : connectionDatas )
            {
                if ( connData.connectionId == conn.connectionId )
                {
                    conn.sendString(xmlKeyRequest);
                    connData.setSentKeyRequest(type, keyString);
                    break;
                }
            }
        }
    }

    public synchronized ConnectionData getConnData(Connection conn)
    {
        for (ConnectionData connData : connectionDatas )
        {
            if ( connData.connectionId == conn.connectionId )
            {
                return connData;
            }
        }
        
        return null;
    }

    public synchronized ConnectionData getConnData(int connId)
    {
        for (ConnectionData connData : connectionDatas )
        {
            if ( connData.connectionId == connId ) 
            {
                return connData;
            }
        }
        
        return null;
    }

    public void setPreferredCipher( String type )
    {
        if ( CipherHandler.supportedCipher( type ) )
        {
            keyHandler.setDefaultTypeString(type);
            sendKeyRequestToAll( type, keyHandler.getDefaultKeyString(type ) );
        }
    }

    public void overrideCipher ( String type, String keyString )
    {
        CipherHandler chIn = CipherHandlerConstructer.getCipherHandler( type, keyString, true);
        CipherHandler chOut = CipherHandlerConstructer.getCipherHandler( type, keyString, false);
        for ( ConnectionData connData : connectionDatas )
        {
            if ( chIn != null )
                connData.setInCipherHandler( chIn );
            if ( chOut != null )
                connData.setOutCipherHandler( chOut );
        }
    }

    public void printCipherHandlers()
    {
        for ( ConnectionData cd : connectionDatas )
        {
            System.out.println(cd.inCipherHandler);
            System.out.println(cd.outCipherHandler);
        }
    }

    public synchronized void sendFileRequest(File f)
    {
        String xmlString = MessageDeparser.getFileRequestString(MainView.getNick(), f );
        filesendtimestamp = (new Date()).getTime();
        fileToSend = f;

        for ( Connection conn : connections )
        {
            conn.sendString(xmlString);
        }
    }

    public void handleFileRequest ( Message m, Connection conn )
    {
        if ( connections.size() > 1 )
        {
            String xmlString = MessageDeparser.getFileResponseString("chat_system", "Du kan ej skicka filer i multipartkonversationer.", false, 0);
            conn.sendString(xmlString);
        }
        else
        {
            (new Thread(new FRHandler(conn, getConnData(conn), m.senderName, m.fileRequestFileName, m.fileRequestFileSize, chat))).start();
        }

    }
    
    public void handleFileResponse ( Message m, Connection conn )
    {
        if ( m.fileResponseReply )
            (new Thread(new FileSender(conn.socket.getInetAddress(), m.fileResponsePort, this.fileToSend, getConnData(conn).outCipherHandler ))).start();
            
        if (m.messageText != null && !"".equals(m.messageText))
            chat.showMessage(m);

    }
}
