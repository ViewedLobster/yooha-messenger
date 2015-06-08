import java.net.*;
import java.io.*;

/*
 * This class constantly reads the incoming strings from the socket and when whole message is matched
 * it forward to the chat controller.
 */

public class ClientInThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private ChatController chatController;

    public ClientInThread(Socket clientSocket, ChatController chatControllerIn){
        socket = clientSocket;
        chatController = chatControllerIn;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e){
            e.printStackTrace();
        }


    }

    public void run(){
        boolean done = false;
        boolean inMessage = false;
        StringBuilder messageString = new StringBuilder();;
        String inputString = null;
        while(!done){
            try {
				inputString = in.readLine();
				if (!inMessage) {
	                // if we are not inside message tags
	                if (inputString.matches("^<message.*")) {
	                    // if find message start tag
	                    messageString.append(inputString);
	                    if (inputString.matches(".*</message>$")) {
	                        // if we also find end tags in same line
	                        inMessage = false;
	                        chatController.receiveMessage(messageString.toString());
	                        messageString = new StringBuilder();
	                    } else {
	                        // else we are in message
	                        inMessage = true;
	                    }
	                }
	            } else {
	                // else we are inside message tags
	                messageString.append(inputString);
	                
	                if (inputString.matches(".*</message>$")) {
	                    // if we find end tag
	                    inMessage = false;
	                    chatController.receiveMessage(messageString.toString());
	                    messageString = new StringBuilder();
	                }
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }

}
