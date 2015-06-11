import java.io.*;
import java.net.*;


public class Server extends Thread{
    private ServerSocket serverSocket;
    private MainController mainController;
    boolean done;

    public Server(int portNo, MainController mainControllerIn){
    	mainController = mainControllerIn;
        newSocket(portNo);
    }

    public void run(){
        listen();
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
        while (!done) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Connection established!");
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
     
     public void shutdown(){
    	done = true;
    	try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("io");
		}
     }
     
     public void newSocket(int portNo){
    	 try {
     		System.out.println("trying to set new socket..");
             serverSocket = new ServerSocket(portNo);
         }catch(IOException e){
             System.out.println("Failed creating server socket");
             //e.printStackTrace();
         }
    	 done = false;
    	 System.out.println("socket set. done set to false");
     }
     
     
}


