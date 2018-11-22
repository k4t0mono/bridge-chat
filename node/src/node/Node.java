package node;

import Chat.Chat;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Node {

    private static final int PORT = 50123;
    public static Gson gson = new GsonBuilder().create();
    
    public static void main(String[] args) {
        try {
            chat_server();
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void chat_server() throws IOException {
        System.out.println("Starting the node");
        ServerSocket ss;
        ss = new ServerSocket(PORT);
        
        while(true) {
            Chat cs;
            cs = new Chat(ss.accept());
            cs.start();
        }
    }
    
}