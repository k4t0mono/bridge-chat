package bridgechat.backend.chat;

import bridgechat.dao.MessageDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketReader extends Thread {

    private final BufferedReader in;
    
    public SocketReader(BufferedReader in) {
        super();
        
        this.in = in;
    }
    
    @Override
    public void run() {
        try {
            String s = in.readLine();
            while(s != null) {
//                System.out.println("> " + s);
                MessageDAO.getInstace().addRecivedMessage(s);
                
                s = in.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(SocketReader.class.getName()).log(Level.FINER, "SocketReader ended");
    }
    
}
