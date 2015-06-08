import java.net.*;
import javax.swing.text.html.*;
import java.io.*;
import javax.swing.text.*;

public class ChatController {
	ChatView theChatView;
        public MessageParser parser;
        public Socket clientSocket;
        Thread clientInThread;
        PrintWriter out;
	
	public ChatController(ChatView chatViewIn, Socket socketIn){
		theChatView = chatViewIn;
                try {
                    parser = new MessageParser();
                } catch(Exception e){
                    e.printStackTrace();
                }

                clientSocket = socketIn;

                try {
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                } catch(Exception e){
                    e.printStackTrace();
                }

                clientInThread = new ClientInThread(clientSocket, this);
                clientInThread.start();
	}
	
        // for testing
        public ChatController(Socket socketIn){
                try {
                    parser = new MessageParser();
                } catch(Exception e){
                    e.printStackTrace();
                }

                clientSocket = socketIn;

                try {
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                } catch(Exception e){
                    e.printStackTrace();
                }


                clientInThread = new ClientInThread(socketIn, this);
                clientInThread.start();
        }

        // for test

        public ChatController(String ip, int port){
            try {
                    parser = new MessageParser();
                } catch(Exception e){
                    e.printStackTrace();
            }

            try {
                clientSocket = new Socket(ip, port);
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch(Exception e){
                e.printStackTrace();
            }

            clientInThread = new ClientInThread(clientSocket, this);
            clientInThread.start();
        }

	public void sendMessage(){
		String textFieldContent = theChatView.getTextFieldContent();
		if (!(textFieldContent == "")){
			Message messageOut = new Message(theChatView.getColor(),textFieldContent,MainView.getNick());
			String XMLString = MessageDeparser.deparseMessage(messageOut);
			addToPane(MessageDeparser.deparseToHTML(messageOut));
                        out.println(XMLString);

		}
	}

        public void sendMessage(String textFieldContent){
		if (!(textFieldContent == "")){
			String XMLString = textFieldContent;
                        out.println(XMLString);
                        System.out.println(XMLString);
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
            try {
                Message message = parser.parseMessageXML(messageString);
                System.out.println(MessageDeparser.deparseToHTML(message));
                addToPane(MessageDeparser.deparseToHTML(message));
            } catch(Exception e){
                e.printStackTrace();
            }
	}


}
