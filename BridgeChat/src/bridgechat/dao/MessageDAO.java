package bridgechat.dao;

import bridgechat.controller.ChatSceneController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    private final List<String> recivedMessages;
    private final List<String> sendedMessages;
    private ChatSceneController chatScene;
    private PrintWriter outSocket;
    
    private static MessageDAO instace = null;
    private int newSendedMSG;
    private int newRecivedMSG;

    private MessageDAO() {
        this.recivedMessages = new ArrayList<>();
        this.newRecivedMSG = 0;
        this.sendedMessages = new ArrayList<>();
        this.newSendedMSG = 0;
    }
    
    public static MessageDAO getInstace() {
        if(instace == null)
            instace = new MessageDAO();
        
        return instace;
    }
    
    public boolean addRecivedMessage(String msg) {
        this.newRecivedMSG++;
        notifyChatScene(msg);
        return this.recivedMessages.add(msg);
    }
    
    public int getLenRecivedMessages() {
        return recivedMessages.size();
    }
    
    public int getNewRecivedMSG() {
        return newRecivedMSG;
    }
    
    public List<String> getRecivedMessages() {
        this.newSendedMSG = 0;
        return recivedMessages;
    }
    
    public boolean addSendedMessage(String msg) {
        this.newSendedMSG++;
        notifyOutSocket(msg);
        return this.sendedMessages.add(msg);
    }
    
    public List<String> getSendedMessages() {
        this.newSendedMSG = 0;
        return sendedMessages;
    }

    public int getNewSendedMSG() {
        return newSendedMSG;
    }

    public void setChatScene(ChatSceneController chatScene) {
        this.chatScene = chatScene;
    }
    
    public void notifyChatScene(String msg) {
        chatScene.insertTextArea(msg);
    }

    public void setOutSocket(PrintWriter outSocket) {
        this.outSocket = outSocket;
    }
    
    public void notifyOutSocket(String msg) {
        if(!outSocket.checkError()) {
            outSocket.println(msg);
        }
    }
    
}
