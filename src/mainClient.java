import java.io.*;
import java.util.Scanner;

public class mainClient {
    public static void main(String[] args) {
        String ip = "localhost";
        int port = 5678;

        ChatController cc = new ChatController(ip, port);

        Scanner si = new Scanner(System.in);

        while (true) {
            cc.sendMessage(si.nextLine());
        }

    }
}
