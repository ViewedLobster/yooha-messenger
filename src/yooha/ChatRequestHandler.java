package yooha;

import yooha.network.Connection;
import yooha.network.Connecter;

import java.io.IOException;

public class ChatRequestHandler implements MessageStringHandler
{
    MainController controller;
    Connecter connecter;
    
    public ChatRequestHandler (MainController controller, int port) 
    {
        this.controller = controller;

        try{
            connecter = new Connecter(port, this);
            Thread t = new Thread( connecter );
            t.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void handleMessageString( String messageString, Connection conn )
    {
        Message m = MessageParser.parseString(messageString);
        System.out.println(m);

        controller.addConnection(m.getText(), conn);
    }

}

