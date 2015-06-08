import java.net.*;
import javax.swing.text.html.*;
import java.io.*;
import javax.swing.text.*;

public class ChatController {
	ChatView theChatView;
        public MessageParser parser;
        public Socket clientSocket;
	
	public ChatController(ChatView chatViewIn, Socket socketIn){
		theChatView = chatViewIn;
        parser = new MessageParser();
	}
	
	public void sendMessage(){
		String textFieldContent = theChatView.getTextFieldContent();
		if (!(textFieldContent == "")){
			Message messageOut = new Message(theChatView.getColor(),textFieldContent,MainView.getNick());
			String XMLString = MessageDeparser.deparseMessage(messageOut);
			addToPane(MessageDeparser.deparseToHTML(messageOut));
		}
	}
	
	public void addToPane(String HTMLString){
		StringReader reader = new StringReader(HTMLString);
		Document doc = theChatView.conversationPane.getDocument();
		
		try{
			theChatView.editor.read(reader, doc, doc.getLength());
		}catch(BadLocationException e){
		}catch(IOException e){
		}
	}
	
	public void receiveMessage(String messageString){
            Message message = parser.parseMessageXML(messageString);
            addToPane(MessageDeparser.deparseToHTML(message));
	}
}
