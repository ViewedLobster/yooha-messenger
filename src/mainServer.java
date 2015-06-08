import java.io.*;
import java.net.*;
import java.util.*;

public class mainServer {

    public static ArrayList<ChatController> ccArray = new ArrayList<ChatController>();

    public static void main(String[] args) {
        int port = 5678;

        Server server = new Server(port);
        server.start();
/*
        Scanner si = new Scanner(System.in);

        while (true) {
            if (!ccArray.isEmpty()) {
                ccArray.get(0).sendMessage(si.nextLine());
            }
        }
        */
    }
}
