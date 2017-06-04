package yooha.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

public class ConnectionClientTest
{


    public static void main(String[] args) {
        
        try{
            Socket s = new Socket("localhost", 12345);

            PrintWriter p = new PrintWriter( s.getOutputStream(), true );

            p.println("<message> hello </message>    jfdjfjdj");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            p.println("<message> , world </message>    jfdjfjdj");


            p.close();

            s.close();
        } catch( Exception e )
        {}


    }
}
