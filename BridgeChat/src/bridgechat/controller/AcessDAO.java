package bridgechat.controller;

import bridgechat.dao.MessageDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcessDAO extends Thread {

    private final MessageDAO dao;
    private final ChatSceneController csc;
    private int numMSG;

    public AcessDAO(ChatSceneController csc) {
        this.dao = MessageDAO.getInstace();
        this.csc = csc;
        this.numMSG = 0;
    }
    
    @Override
    public void run() {
        while(true) {
            int n = dao.getNewSendedMSG();
            if(n > 0) {
                List<String> messages = dao.getRecivedMessages();
                for(int i = numMSG; i < messages.size(); i++) {
                    csc.insertTextArea(messages.get(i));
                }
                numMSG += n;
            }
            
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(AcessDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
