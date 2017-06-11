package yooha;

import yooha.network.Connection;
import yooha.network.FileSender;
import yooha.cipher.CipherHandlerConstructer;
import yooha.cipher.CipherHandler;
import yooha.cipher.KeyHandler;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Date;


public class ClientBackend extends ChatBackend implements MessageStringHandler
{
    private Connection conn;
    ConnectionData connectionData;

    File fileToSend;
    long filesendtimestamp;

    public ClientBackend( Connection conn, int chat_id ){
        this.conn = conn;
        this.chat_id = chat_id;
        this.connectionDatas = new ArrayList<ConnectionData>();
        this.conn.setMessageHandler(this);
        this.connectionData = new ConnectionData("chat_server", null, conn.connectionId);
        this.keyHandler = new KeyHandler();
        this.encrypt = false;
    }

    public void sendMessage( Message message )
    {

        CipherHandler ch;
        if ( this.encrypt )
        {
            ch = connectionData.outCipherHandler;
        }
        else
        {
            ch = null;
        }
        String xml = MessageDeparser.deparseMessage(message, ch);
        conn.sendString(xml);
    }

    public void shutdown()
    {
        conn.sendString(MessageDeparser.getDisconnectXml("chat_system"));
        conn.shutdown();
    }

    public List<ConnectionData> getConnectionData()
    {
        return this.connectionDatas;
    }

    public void handleMessageString(String messageString, Connection conn )
    {
        // parse string
        // check for disconnect
        // if not then send on to Chat
        
        
        Message m = MessageParser.parseString(messageString, connectionData.inCipherHandler);

        if (m != null )
        {
            if (m.disconnect)
            {
                conn.shutdown();
                chat.showMessage(MessageParser.getDisconnectMessage(m.senderName));
                System.out.println(m);
            }
            else if (m.request)
            {
                if (m.requestDenied)
                {
                    conn.shutdown();
                    chat.showMessage(MessageParser.getRequestDeniedMessage(m.senderName));
                }
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

                        System.err.println("Received keyrequest reply: " + m.keyReply);

                    }
                    else if ( "supported".equals( m.keyReply )  )
                    {   // If the reply sais supported, set cipher handlers
                        ConnectionData cd  = this.connectionData;
                        // Only set cipherhandlers if the sent key request type matches the reply received
                        System.err.println("Received keyrequest reply: " + m.keyReply);
                        if ( cd.hasSentKeyRequest(m.keyType) )
                        {
                            System.err.println("Setting cipherhandlers.");
                            cd.setOutCipherHandler( CipherHandlerConstructer.getCipherHandler( m.keyType, m.key, false ));
                            cd.setInCipherHandler( CipherHandlerConstructer.getCipherHandler(m.keyType, cd.keyRequestKey, true) );
                        }
                    }
                }
                else
                {
                    if ( CipherHandler.supportedCipher(m.keyType) )
                    {
                        ConnectionData cd = this.connectionData;
                        // We set outgoing cipher handler and get the default key from the keyhandler ( matching the specified cipher type )
                        // We then send this back tho the requester
                        cd.setOutCipherHandler(CipherHandlerConstructer.getCipherHandler( m.keyType, m.key, false ));
                        String key = keyHandler.getDefaultKeyString(m.keyType);
                        cd.setInCipherHandler(CipherHandlerConstructer.getCipherHandler(m.keyType, key, true));
                        String xmlReply = MessageDeparser.getKeyRequestReplyString( MainView.getNick(), true, m.keyType, key);
                        conn.sendString(xmlReply);
                        System.err.println("Received keyrequest, replied: "+xmlReply);
                    }
                    else
                    {
                        // If the cipher is not supported we send reply saying so.
                        String xmlReply = MessageDeparser.getKeyRequestReplyString( MainView.getNick(), false, m.keyType, null );
                        conn.sendString(xmlReply);
                        System.err.println("Received keyrequest, replied: "+xmlReply);
                    }
                }
                
                System.out.println(connectionData.inCipherHandler);
                System.out.println(connectionData.outCipherHandler);
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
                chat.showMessage(m);
                System.out.println(m);
            }
        }
    }

    public void sendDisconnect()
    {
        conn.sendString(MessageDeparser.getDisconnectXml(MainView.getNick()));
    }

    public void setPreferredCipher( String type )
    {
        if (CipherHandler.supportedCipher(type))
        {
            keyHandler.setDefaultTypeString(type);
            sendKeyRequest(type, keyHandler.getDefaultKeyString(type));
        }
    }

    public void sendKeyRequest(String type, String keyString )
    {
        String xml = MessageDeparser.getKeyRequestString(MainView.getNick(), type, keyString);
        connectionData.setSentKeyRequest(type, keyString);
        conn.sendString( xml );
    }

    public void overrideCipher( String type, String keyString )
    {
        CipherHandler ch = CipherHandlerConstructer.getCipherHandler(type, keyString, true);
        if (ch != null)
            connectionData.setInCipherHandler(ch);
        ch = CipherHandlerConstructer.getCipherHandler(type, keyString, false);
        if (ch != null)
            connectionData.setOutCipherHandler(ch);

    }

    public synchronized void sendFileRequest(File f, String text)
    {
        String xmlString = MessageDeparser.getFileRequestString(MainView.getNick(), f, text );
        filesendtimestamp = (new Date()).getTime();
        fileToSend = f;

        conn.sendString(xmlString);
    }

    public void handleFileRequest ( Message m, Connection conn )
    {
        (new Thread(new FRHandler(conn, connectionData, m.senderName, m.fileRequestFileName, m.fileRequestFileSize, chat, m.getText() ))).start();
    }

    public void handleFileResponse ( Message m, Connection conn )
    {
        if ( m.fileResponseReply )
            (new Thread(new FileSender(conn.socket.getInetAddress(), m.fileResponsePort, this.fileToSend, connectionData.outCipherHandler ))).start();
            
        if (m.messageText != null && !"".equals(m.messageText))
            chat.showMessage(m);
    }

}
