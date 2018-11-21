package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends Thread {
    
    private final Socket socket;

    public ChatClient(Socket s) {
        this.socket = s;
    }
    
    @Override
    public void run() {
        System.out.println("New ChatClient Started");
        
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(this.socket.getInputStream())
            );
            Scanner scan = new Scanner(System.in);
            
            String input_line;
            while((input_line = scan.nextLine()) != null) {
                if(input_line.equals("."))
                    break;
                
                System.out.println(in.readLine());
            }
           
            scan.close();
//            in.close();
            out.close();
            this.socket.close();
            
        } catch (IOException ex) {
            System.err.println("Problem with Communication Server");
            System.exit(1); 
        }
    }
    
}
