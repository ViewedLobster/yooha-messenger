package yooha.network;

import yooha.MessageStringHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Connecter implements Runnable
{
    int port;
    ServerSocket serverSocket;
    MessageStringHandler handler;
    private static int connectionCount;

    public static synchronized int getFreeConnectionId()
    {
        return connectionCount++;
    }

    public Connecter ( int port, MessageStringHandler handler ) throws IOException
    {
        this.port = port;
        this.handler = handler;
        this.connectionCount = 0;

        serverSocket = new ServerSocket(this.port);
    }


    public void run()
    {
        listen();
    }

    private void listen()
    {
        boolean done = false;

        while (!done)
        {

            Socket s = null;

            try
            {
                s = serverSocket.accept();

                // Create connection
                Connection c = new Connection(s, getFreeConnectionId(), handler);
                
                Thread t = new Thread(c);
                t.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

