package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketReader extends Thread {

    private final Chat parent;
    
    public SocketReader(Chat parent) {
        super();
        
        this.parent = parent;
    }
    
    @Override
    public void run() {
        try {
            String s = parent.getIn().readLine();
            while(s != null) {
                System.out.println(s);
                
                s = parent.getIn().readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);    
        }
        
        try {
            this.parent.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(parent.getSocket().isInputShutdown());
        System.out.println(parent.getSocket().isOutputShutdown());
        System.out.println("morri x.x");
        this.parent.stop();
    }
    
}
