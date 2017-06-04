package yooha.network;

import java.net.ServerSocket;
import java.net.Socket;
import yooha.MessageStringPrinter;

public class ConnectionTest
{


    public static void main(String[] args) {
        
        try{
            ServerSocket ss = new ServerSocket(12345);

            Socket s = ss.accept();

            Thread t =new Thread( new Connection(s, 0, new MessageStringPrinter() ) );

            t.start();

            ss.close();
        } catch (Exception e)
        {}
    }
}
