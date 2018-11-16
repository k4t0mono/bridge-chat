package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer extends Thread {
    
    private final int PORT = 50123;
    private final Socket socket;

    public ChatServer(Socket s) {
        this.socket = s;
    }
    
    @Override
    public void run() {
        System.out.println ("New Communication Thread Started");
        
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
            Scanner scan = new Scanner(System.in);
            
            String input_line;
            while((input_line = in.readLine()) != null) {
                if(input_line.equals("."))
                    break;
                
                System.out.println(input_line);
                String res;
                res = scan.nextLine();
                
                long time = System.currentTimeMillis();
                Message msg;
                msg = new Message("test", "asdasd", time, res);
                out.println(node.Node.gson.toJson(msg));
            }
           
            scan.close();
            in.close();
            out.close();
            this.socket.close();
            
        } catch (IOException ex) {
            System.err.println("Problem with Communication Server");
            System.exit(1); 
        }
    }
    
}
