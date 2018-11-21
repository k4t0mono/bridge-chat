package node;

import Chat.ChatServer;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Node {

    private static final int PORT = 50123;
    public static Gson gson = new GsonBuilder().create();
    
    public static void main(String[] args) {
        System.out.println("0 - test chat server");
        System.out.println("1 - test chat client");
        
        Scanner scan = new Scanner(System.in);
        int op = scan.nextInt();

        try {
            switch(op) {
            case 0:
                chat_server();
                break;
            
            case 1:
                chat_client();
                break;
            }
        } catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        scan.close();
    }
    
    private static void chat_server() throws IOException {
        System.out.println("Starting the node");
        ServerSocket ss;
        ss = new ServerSocket(PORT);
        
        while(true) {
            ChatServer cs;
            cs = new ChatServer(ss.accept());
            cs.start();
        }
    }
    
    private static void chat_client() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
