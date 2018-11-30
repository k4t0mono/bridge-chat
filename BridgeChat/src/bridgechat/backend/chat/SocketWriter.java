package bridgechat.backend.chat;

import bridgechat.dao.MessageDAO;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketWriter extends Thread {
    
    private final PrintWriter out;
    
    public SocketWriter(PrintWriter out) {
        super();
        
        this.out = out;
    }
    
    @Override
    public void run() {
        int n;
        int numMSG = 0;
        MessageDAO dao = MessageDAO.getInstace();
        while(true) {
            n = dao.getNewSendedMSG();
            if(n > 0) {
                List<String> messages = dao.getSendedMessages();
                for(int i = numMSG; i < messages.size(); i++) {
                    out.println(messages.get(i));
                }
                numMSG += n;
            }
            
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
