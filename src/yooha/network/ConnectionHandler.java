package yooha.network;


import java.util.List;
import java.util.ArrayList;



public class ConnectionHandler 
{

    List<Connection> connections;

    public ConnectionHandler()
    {
        connections = new ArrayList<Connection>();
    }

    public synchronized void addConnection( Connection conn )
    {
        connections.add(conn);
    }
    

    public synchronized void sendString(String s)
    {
        for( Connection conn : connections )
            conn.sendString(s);
    }


}
