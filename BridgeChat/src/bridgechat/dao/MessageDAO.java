package bridgechat.dao;

import bridgechat.dao.exception.InvalidReciverException;
import bridgechat.dao.exception.DaoException;
import bridgechat.dao.exception.InvalidMessageException;
import bridgechat.backend.Node;
import bridgechat.backend.chat.Chat;
import bridgechat.backend.chat.Message;
import bridgechat.controller.ChatSceneController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessageDAO {
    
    private ChatSceneController chatScene;
    private final UserDAO userDAO;
    
    private final Map<String, List<Message>> histories;
    private final Map<String, Chat> chats;
    private String activeUser;
    
    private static MessageDAO instace = null;
    private static final Gson GSON = new GsonBuilder().create();

    
    private MessageDAO() {
        this.histories = new HashMap<>();
        this.chats = new HashMap<>();
        this.userDAO = UserDAO.getInstance();
    }
    
    public static MessageDAO getInstace() {
        if(instace == null)
            instace = new MessageDAO();
        
        return instace;
    }
    
    public Iterator getHistoryIterator(String login) {
        if(histories.get(login) == null)
            histories.put(login, new ArrayList<>());
        
        return histories.get(login).iterator();
    }
    
    public void addSended(String login, String s) {
        long timeStamp = Instant.now().getEpochSecond();
        Message msg = new Message(userDAO.getUsername(), activeUser, timeStamp, s);
        
        if(histories.get(login) == null)
            histories.put(login, new ArrayList<>());
        
        histories.get(login).add(msg);
        notifyOutSocket(msg, login);
    }
    
    public void addRecived(Message msg) throws DaoException {
        if(msg == null || !msg.isValid())
            throw new InvalidMessageException("Recived invalid message");
        
        if(!msg.getReciver().equals(userDAO.getUsername()))
            throw new InvalidReciverException(msg.getReciver(), userDAO.getUsername());
        
        String login = msg.getSender();
        if(histories.get(login) == null)
            histories.put(login, new ArrayList<>());
        
        histories.get(login).add(msg);
        
        if(msg.getSender().equals(activeUser))
            notifyChatScene(msg);
        else
            chatScene.adcValueForUser(msg.getSender());
    }

    public void setChatScene(ChatSceneController chatScene) {
        this.chatScene = chatScene;
    }
    
    public void notifyChatScene(Message msg) {
        chatScene.addMessage(msg.getSender(), msg.getText());
    }
    
    public void notifyOutSocket(Message msg, String login) {
        chats.forEach((String key, Chat value) -> {
            System.out.println(key + " -> " + value);
        });
        
        PrintWriter out = chats.get(login).getOut();
        if(!out.checkError()) {
            out.println(GSON.toJson(msg));
        }
    }

    public void addChat(String login, Chat c) {
        chats.put(login, c);
    }
    
    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }

    public void addUser(String username) {
        chatScene.adcTableValue(username, 0);
    }
    
}
