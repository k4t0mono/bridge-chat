package bridgechat.backend.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat extends Thread {
    
    private final Socket socket;

    public Chat(Socket s) {
        this.socket = s;
    }
    
    @Override
    public void run() {
        System.out.println("New ChatServer Started for " + this.socket.getInetAddress());
        
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
            
            SocketReader sr = new SocketReader(in);
            SocketWriter sw = new SocketWriter(out);
            
            sr.start();
            sw.start();
            
            sr.join();
//            sw.interrupt();
            sw.join();
            
            in.close();
            out.close();
            this.socket.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(SocketReader.class.getName()).log(Level.FINER, "Chat ended");
    }
    
}
