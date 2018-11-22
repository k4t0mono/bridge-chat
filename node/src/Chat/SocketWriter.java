package Chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketWriter extends Thread {
    
    private final Chat parent;
    
    public SocketWriter(Chat parent) {
        super();
        
        this.parent = parent;
    }
    
    @Override
    public void run() {
//        try {
//            
//        } catch (IOException ex) {
//            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);    
//        }
    }
    
}
