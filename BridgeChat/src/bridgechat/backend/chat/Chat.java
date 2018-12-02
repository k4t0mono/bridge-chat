package bridgechat.backend.chat;

import bridgechat.backend.Node;
import bridgechat.dao.MessageDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat extends Thread {
    
    private final Socket socket;
    private String username;
    private PrintWriter out;

    public Chat(Socket s, String username) {
        this.socket = s;
        this.username = username;
    }
    
    public Chat(Socket s) {
        this(s, null);
    }
    
    @Override
    public void run() {
        System.out.println("New ChatServer Started for " + this.socket.getInetAddress());
        
        try {
            out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
            
            MessageDAO dao = MessageDAO.getInstace();
            
            if(username != null) {
                out.println(username);
                System.out.println("Username sended");
            } else {
                username = in.readLine();
                dao.addUser(username);
                System.out.println("Got username: " + username);
            }
            dao.addChat(username, this);
            
            SocketReader sr = new SocketReader(in);
            sr.start();
            sr.join();
            
            in.close();
            out.close();
            this.socket.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(SocketReader.class.getName()).log(Level.FINER, "Chat ended");
    }

    public PrintWriter getOut() {
        return out;
    }
    
}
