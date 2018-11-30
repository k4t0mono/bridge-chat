package bridgechat.dao;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    private final List<String> recivedMessages;
    private final List<String> sendedMessages;
    private static MessageDAO instace = null;
    private int newSendedMSG;
    private int newRecivedMSG;

    private MessageDAO() {
        this.recivedMessages = new ArrayList<>();
        this.sendedMessages = new ArrayList<>();
        this.newSendedMSG = 0;
        this.newRecivedMSG = 0;
    }
    
    public static MessageDAO getInstace() {
        if(instace == null)
            instace = new MessageDAO();
        
        return instace;
    }
    
    public boolean addRecivedMessage(String msg) {
        this.newRecivedMSG++;
        return this.recivedMessages.add(msg);
    }
    
    public List<String> getRecivedMessages() {
        this.newSendedMSG = 0;
        return recivedMessages;
    }
    
    public int getNewRecivedMSG() {
        return newRecivedMSG;
    }
    
    public boolean addSendedMessage(String msg) {
        System.out.println("addSendedMessage");
        this.newSendedMSG++;
        return this.sendedMessages.add(msg);
    }
    
    public List<String> getSendedMessages() {
        this.newSendedMSG = 0;
        return sendedMessages;
    }
    
    public int getLenRecivedMessages() {
        return recivedMessages.size();
    }

    public int getNewSendedMSG() {
        return newSendedMSG;
    }
    
}
