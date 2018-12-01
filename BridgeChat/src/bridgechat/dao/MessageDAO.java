package bridgechat.dao;

import bridgechat.backend.Node;
import bridgechat.backend.chat.Message;
import bridgechat.controller.ChatSceneController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageDAO {
    
    private final List<Message> history;
    private ChatSceneController chatScene;
    private PrintWriter outSocket;
    private String chatUsername;
    
    private static MessageDAO instace = null;
    private static final Gson GSON = new GsonBuilder().create();

    private MessageDAO() {
        this.history = new ArrayList<>();
    }
    
    public static MessageDAO getInstace() {
        if(instace == null)
            instace = new MessageDAO();
        
        return instace;
    }
    
    public Iterator getHistoryIterator() {
        return history.iterator();
    }
    
    public void addSended(String s) {
        long timeStamp = Instant.now().getEpochSecond();
        Message msg = new Message(Node.getUsername(), chatUsername, timeStamp, s);
        history.add(msg);
        notifyOutSocket(msg);
    }
    
    public void addRecived(Message msg) throws DaoException {
        if(msg == null || !msg.isValid())
            throw new InvalidMessageException("Recived invalid message");
        
        if(!msg.getReciver().equals(Node.getUsername()))
            throw new InvalidReciverException(msg.getReciver(), Node.getUsername());
        
        history.add(msg);
        notifyChatScene(msg);
    }

    public void setChatScene(ChatSceneController chatScene) {
        this.chatScene = chatScene;
    }
    
    public void notifyChatScene(Message msg) {
        chatScene.insertMessage(msg);
    }

    public void setOutSocket(PrintWriter outSocket) {
        this.outSocket = outSocket;
    }
    
    public void notifyOutSocket(Message msg) {
        if(!outSocket.checkError()) {
            outSocket.println(GSON.toJson(msg));
        }
    }

    public void setChatUsername(String chatUsername) {
        this.chatUsername = chatUsername;
    }
    
}
