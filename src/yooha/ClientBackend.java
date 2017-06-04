package yooha;

import yooha.network.Connection;

import java.util.ArrayList;
import java.util.List;

public class ClientBackend extends ChatBackend implements MessageStringHandler
{
    private Connection conn;

    public ClientBackend( Connection conn, int chat_id ){
        this.conn = conn;
        this.chat_id = chat_id;
        this.connectionDatas = new ArrayList<ConnectionData>();
        this.conn.setMessageHandler(this);
    }

    public void sendMessage( Message message )
    {

        String xml = MessageDeparser.deparseMessage(message);
        conn.sendString(xml);
    }

    public void shutdown()
    {
        //TODO implement this
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
        
        Message m = MessageParser.parseString(messageString);

        if (m != null )
        {
            if (m.disconnect)
            {
                conn.shutdown();
                chat.showMessage(m);
                System.out.println(m);
            }
            else
            {
                chat.showMessage(m);
                System.out.println(m);
            }
        }
    }

}
