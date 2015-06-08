import javax.swing.*;

public class MainController extends JTabbedPane{
		MainView theMainView;
		static int currentIndex = 0;
		
		public MainController(int listeningPort, MainView mainViewIn){
			theMainView = mainViewIn;
		}
		
		public void newChat(MainModel mainModelIn){
			ChatView newChatView = new ChatView(currentIndex);
			this.insertTab(mainModelIn.getNick(), null, newChatView, null, currentIndex++);
		}
}
