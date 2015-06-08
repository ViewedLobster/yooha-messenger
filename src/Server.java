import java.io.*;
import java.net.*;


public class Server {
    private ServerSocket serverSocket;
    private MainController mainController;

    public Server(int portNo, MainController mainControllerIn){
    	mainController = mainControllerIn;
        try {
            serverSocket = new ServerSocket(portNo);
        } catch(IOException e){
            System.out.println("Failed creating server socket");
            e.printStackTrace();
        }

        listen();
    }

    private void listen(){
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                mainController.newChat(MainView.exampleModel, clientSocket);
                //Thread client = new ClentThread(clientSocket);
                //client.start();
            } catch(IOException e){
                e.printStackTrace();
            }    
        }
        
}

