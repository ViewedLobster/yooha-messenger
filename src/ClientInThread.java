import java.net.*;
import java.io.*;

public class ClientInThread extends Thread {
    
    private Socket socket;

    public ClientInThread(Socket clientSocket){
        socket = clientSocket;
    }

}
