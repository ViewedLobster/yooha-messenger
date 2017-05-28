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

    public void addConnection( Connection conn )
    {
        connections.add(conn);
    }
    

}
