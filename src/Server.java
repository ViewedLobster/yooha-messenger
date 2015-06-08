import java.io.*;
import java.net.*;


public class Server {
    private ServerSocket serverSocket;

    public Server(int portNo){
        try {
            serverSocket = new ServerSocket(portNo);
        } catch(IOException e){
            System.out.prinln("Failed creating server socket");
            e.printStackTrace();
        }

        listen();
    }

    private void listen(){
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                Thread client = new ClentThread(clientSocket);
                client.start();
            } catch(IOException e){
                e.printStackTrace();
            }    
        }
        
}

