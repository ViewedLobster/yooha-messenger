import java.io.*;
import java.net.*;


public class Server extends Thread{
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

    }

    public void run(){
        listen2();
    }

    public Server(int portNo){
        try {
            serverSocket = new ServerSocket(portNo);
        } catch(IOException e){
            System.out.println("Failed creating server socket");
            e.printStackTrace();
        }

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

     private void listen2(){
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Connection established!");
                ChatController cc = new ChatController(clientSocket);
                mainServer.ccArray.add(cc);
                //mainController.newChat(MainView.exampleModel, clientSocket);
                //Thread client = new ClentThread(clientSocket);
                //client.start();
            } catch(IOException e){
                e.printStackTrace();
            }    
        }

     }
        
}

