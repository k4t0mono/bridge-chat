package node;

import Chat.ChatServer;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;

public class Node {

    private static final int PORT = 50123;
    public static Gson gson = new GsonBuilder().create();
    
    public static void main(String[] args) throws IOException {
        System.out.println("Starting the node");
        ServerSocket ss;
        ss = new ServerSocket(PORT);
        
        while(true) {
            ChatServer cs;
            cs = new ChatServer(ss.accept());
            cs.start();
        }
    }
    
}
