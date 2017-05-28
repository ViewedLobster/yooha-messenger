package yooha;


import yooha.Message;
import yooha.network.Connection;
import yooha.network.ConnectionHandler;


public class ServerBackend implements ChatBackend
{

    ConnectionHandler handler;

    public ServerBackend( Connection conn )
    {
        this.handler = new ConnectionHandler();
        this.handler.addConnection(conn);
    }

    public void sendMessage(Message message){
        String xmlMessage = MessageDeparser.deparseMessage(message);

        handler.sendString(xmlMessage);
    }

    public void addConnection(Connection conn)
    {
        handler.addConnection(conn);
    }
}
