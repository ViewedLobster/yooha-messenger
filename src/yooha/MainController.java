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

    public void addConnection(String requestText, Connection conn)
    {
        // TODO
        // Show some dialogue that lets you choose where to add conn
        
        ServerBackend sb = new ServerBackend(conn, getFreeChatId());

        newChat(MainView.exampleModel, sb);

    }

    public void connect( String host, int port )
    {
        try {
            System.out.println("Connecting...");
            Socket s = new Socket(host, port);
            Connection conn = new Connection( s, Connecter.getFreeConnectionId() );
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
        chats.add(c);

        this.addTab("Chat", null, c, null);
    }
}

