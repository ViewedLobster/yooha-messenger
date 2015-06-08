import javax.swing.*;
import java.net.*;

public class MainController extends JTabbedPane{
		MainView theMainView;
		Server server;
		static int currentIndex = 0;
		
		public MainController(int listeningPort, MainView mainViewIn){
			theMainView = mainViewIn;
			server = new Server(listeningPort,this);
		}
		
		public void newChat(MainModel mainModelIn, Socket socketIn){
			ChatView newChatView = new ChatView(currentIndex,socketIn);
			this.insertTab(mainModelIn.getNick(), null, newChatView, null, currentIndex++);
		}
}
