package yooha.network;

import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

import yooha.MessageStringHandler;

public class Connection implements Runnable {

    public Socket socket;
    public InputStream in;
    public PrintWriter out;
    private Scanner inputScanner;
    public int connectionId;
    private MessageStringHandler messageHandler;

    public int horizon = 16384;

    private final Pattern messagePattern = Pattern.compile("<\\s*message.*>.*</message>");

    public Connection ( Socket socket, int connectionId, MessageStringHandler mh ) throws IOException
    {
        this.socket = socket;
        this.connectionId = connectionId;
        this.messageHandler = mh;

        in = socket.getInputStream();
        inputScanner = new Scanner(in, "utf-8");
        out = new PrintWriter ( this.socket.getOutputStream());
    }

    public Connection ( Socket socket, int connectionId) throws IOException
    {
        this.socket = socket;
        this.connectionId = connectionId;
        this.messageHandler = null;

        in = socket.getInputStream();
        inputScanner = new Scanner(in, "utf-8");
        out = new PrintWriter ( this.socket.getOutputStream() );
    }

    public void setMessageHandler( MessageStringHandler mh )
    {
        this.messageHandler = mh;
    }

    public void run()
    {
        parseInStream();
    }

    public void parseInStream()
    {
        boolean done = false;

        while (!done)
        {
            String messageString = inputScanner.findWithinHorizon(this.messagePattern, this.horizon);

            if (messageString != null)
            {
                System.out.println(messageString);
                receiveMessage(messageString);
            }
            else
            {
                shutdown();
                done = true;
            }
        }
        System.err.println("Connection thread ended!");
    }

    private void receiveMessage(String messageString) {
        this.messageHandler.handleMessageString(messageString, this);
    }

    public void sendString(String s )
    {
        System.out.println("Sending string: "+s);
        out.println(s);
        out.flush();
    }

    public void shutdown()
    {
        System.err.println("Shutting down connection "+connectionId);
        try{
            in.close();
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


}
