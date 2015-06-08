
public class ChatController {
	ChatView theChatView;
	
	public ChatController(ChatView chatViewIn){
		theChatView = chatViewIn;
	}
	
	public void sendMessage(){
		Message messageOut = new Message(theChatView.getColor(),theChatView.getTextFieldContent(),MainView.getNick());
		MessageDeparser.deparseMessage(messageOut);
	}
}
