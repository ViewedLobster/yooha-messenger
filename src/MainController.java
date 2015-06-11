import javax.swing.*;
import java.net.*;
import java.io.*;

public class MainController extends JTabbedPane{
		MainView theMainView;
		Server server;
		static int currentIndex = 0;
		
		public MainController(int listeningPort, MainView mainViewIn){
			theMainView = mainViewIn;
			server = new Server(listeningPort,this);
                        server.start();
		}
		
		public void newChat(MainModel mainModelIn, Socket socketIn){
			ChatView newChatView = new ChatView(this ,socketIn);
			this.addTab(mainModelIn.getNick(), null, newChatView, null);
		}
		
		public void connect(String hostAddress, int portNo){
			Socket clientSocket = null;
			try{
				clientSocket = new Socket(hostAddress,portNo);
			}catch(UnknownHostException e){
				System.out.println("Host unknown");
			}catch(IOException e){
				System.out.println("Failed creating client socket");
			}
			this.newChat(MainView.exampleModel,clientSocket);
		}
		
		public void serverRestart(int portNo){
			server.shutdown();
			server = new Server(portNo,this);
			server.start();
		}
}
