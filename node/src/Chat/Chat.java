package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Chat extends Thread {
    
    private final Socket socket;
    private BufferedReader in;

    public BufferedReader getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    public Chat(Socket s) {
        this.socket = s;
    }
    
    @Override
    public void run() {
        System.out.println("New ChatServer Started for " + this.socket.getInetAddress());
        
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
//            SocketReader sr = new SocketReader(this);
            Scanner scan = new Scanner(System.in);
            
            String s = scan.nextLine();
            out.println(s);
            System.out.println(this.socket.get);
//            
//            sr.start();
//            String string;
//            while((string = scan.nextLine()) != null) {
//                if(string.equals("."))
//                    break;
//                
//                out.println(string);
//            }
//            
            scan.close();
            in.close();
            out.close();
            this.socket.close();
            System.out.println("ChatServer ended");
        } catch (IOException ex) {
            System.err.println("Problem with Communication Server");
            System.exit(1); 
        }
    }
    
}
