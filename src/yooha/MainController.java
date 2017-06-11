package yooha;

import yooha.network.Connection;
import yooha.network.Connecter;

import javax.swing.JTabbedPane;
import java.util.List;
import java.util.ArrayList;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;


public class MainController extends JTabbedPane
{
    //MainView mainView;
    ChatRequestHandler chatRequestHandler;
    private int currentChatIndex = 0;
    List<Chat> chats = new ArrayList<Chat>();
    MainView mainView;

    public MainController( int listeningPort, MainView mainView )
    {
        this.mainView = mainView;
        
        chatRequestHandler = new ChatRequestHandler( this, listeningPort );
    }
    
    private synchronized int getFreeChatId()
    {
        return currentChatIndex++;
    }

    public void addConnection(Message message, Connection conn)
    {
        
        int chat_id = AddToChatHelper.whatChat(chats, message);
        if (chat_id == -1)
        {
            // -1 means create new chat
            ServerBackend sb = new ServerBackend(conn, message.senderName, getFreeChatId());

            newChat(MainView.exampleModel, sb);
        }
        else if(chat_id == -2)
        {
            // -2 means reject connection
            String xmlReject = message.request ? MessageDeparser.getRequestRejection() : MessageDeparser.getSimpleRejection();
            conn.sendString(xmlReject);
            conn.shutdown();
        }
        else
        {
            for (Chat c : chats )
            {
                if ( c.chatBackend.chat_id == chat_id && c.server )
                {
                    ((ServerBackend) c.chatBackend).addConnection(conn, message.senderName);
                    break;
                }
            }
        }

    }

    public void connect( String host, int port )
    {
        try {
            System.out.println("Connecting...");
            Socket s = new Socket(host, port);
            Connection conn = new Connection( s, Connecter.getFreeConnectionId() );
            conn.sendString(MessageDeparser.getRequestString(MainView.getNick()));
            (new Thread(conn)).start();
            ClientBackend cb = new ClientBackend(conn, getFreeChatId());

            newChat(MainView.exampleModel, cb);
        }
        catch (UnknownHostException e)
        { e.printStackTrace();}
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void newChat(MainModel mainModel, ChatBackend cb )
    {
        Chat c = new Chat(this, cb);
        synchronized(chats){
            chats.add(c);
        }

        this.addTab("Chat "+cb.chat_id, null, c, null);
    }

    public void shutdown()
    {
        for ( Chat c : chats )
        {
            c.shutdown();
        }
        System.exit(0);
    }

    public void removeChat( Chat c )
    {
        synchronized(chats)
        {
            chats.remove(c);
        }
    }
}

