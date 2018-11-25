package node.chat;

import java.io.PrintWriter;
import java.util.Scanner;

public class SocketWriter extends Thread {
    
    private final PrintWriter out;
    
    public SocketWriter(PrintWriter out) {
        super();
        
        this.out = out;
    }
    
    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        
        String s;
        while(!this.out.checkError()) {
            s = scan.nextLine();
            this.out.println(s);
        }
        
        System.out.println("dead");
        scan.close();
    }
    
}
