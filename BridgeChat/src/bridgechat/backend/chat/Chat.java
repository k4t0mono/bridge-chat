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
    private final String username;

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
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
            
            MessageDAO dao = MessageDAO.getInstace();
            dao.setOutSocket(out);
            
            if(username != null) {
                out.println(username);
                System.out.println("Username sended");
            } else {
                String s = in.readLine();
                dao.setChatUsername(s);
                System.out.println("Got username: " + s);
            }
            
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
    
}
