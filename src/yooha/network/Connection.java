package yooha.network;

import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Connection implements Runnable {

    public Socket socket;
    public InputStream in;
    public PrintWriter out;
    private Scanner inputScanner;
    public int chat_id;

    public int horizon = 16384;

    private final Pattern messagePattern = Pattern.compile("<\\s*message.*>.*</message>");

    public Connection ( Socket socket, int chat_id ) throws IOException
    {
        this.socket = socket;
        this.chat_id = chat_id;

        in = socket.getInputStream();
        inputScanner = new Scanner(in, "utf-8");
        out = new PrintWriter ( this.socket.getOutputStream() );
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
                receiveMessage(messageString);
            }
            else
            {
                shutdown();
            }
        }
    }

    private void receiveMessage(String messageString) {
        // TODO Auto-generated method stub

    }

    public void shutdown()
    {
        inputScanner.close();
        out.close();
    }


}
