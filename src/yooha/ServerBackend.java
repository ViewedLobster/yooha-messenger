package yooha;


import yooha.Message;
import yooha.network.Connection;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;


public class ServerBackend extends ChatBackend implements MessageStringHandler
{

    List<Connection> connections;

    public ServerBackend( int chat_id )
    {
        this.chat_id = chat_id;
        connections = new LinkedList<Connection>();
        this.connectionDatas = new ArrayList<ConnectionData>();
    }
    
    public ServerBackend( Connection conn, int chat_id )
    {
        this.chat_id = chat_id;
        connections = new LinkedList<Connection>();
        this.connectionDatas = new ArrayList<ConnectionData>();
        addConnection(conn);
    }

    public synchronized void sendMessage(Message message){
        String xmlMessage = MessageDeparser.deparseMessage(message);

        for ( Connection conn : connections )
        {
            System.out.println("sending message");
            conn.sendString(xmlMessage);
        }
    }

    public synchronized void addConnection(Connection conn)
    {
        conn.setMessageHandler(this);
        connections.add(conn);
        connectionDatas.add( new ConnectionData( null, null, conn.connectionId) );
    }

    public void handleMessageString( String messageString, Connection conn )
    {
        // TODO
        // Send the message to all connections except sending one
        // Parse the message
        Message m = MessageParser.parseString(messageString);
        if (m != null )
        {
            if ( !m.disconnect )
            {
                synchronized ( this )
                {
                    for ( Connection c : connections )
                    {
                        if ( c.connectionId != conn.connectionId )
                        {
                            c.sendString(messageString);
                        }
                    }
                }
                chat.showMessage(m);
            }
            else
            {
                conn.shutdown();
                removeConnection( conn );
                chat.showMessage(m);
            }
        }
        
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
                break;
            }
        }

        if ( !removed )
        {
            System.err.println("Did not remove connection (connectionId: " + conn.connectionId +")");
        }
    }

    public void shutdown()
    {
        // TODO implement
    }

    public List<ConnectionData> getConnectionData()
    {
        return this.connectionDatas;
    }
}
