package bridgechat.dao;

import bridgechat.dao.exception.InvalidReciverException;
import bridgechat.dao.exception.DaoException;
import bridgechat.dao.exception.InvalidMessageException;
import bridgechat.backend.Node;
import bridgechat.backend.chat.Chat;
import bridgechat.backend.chat.Message;
import bridgechat.backend.tracker.OnlineUser;
import bridgechat.controller.ChatSceneController;
import bridgechat.util.SceneManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class MessageDAO {
    
    private ChatSceneController chatScene;
    private final UserDAO userDAO;
    
    private final Map<String, List<Message>> histories;
    private final Map<String, Chat> chats;
    private String activeUser;
    
    private static MessageDAO instace = null;
    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());

    
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
        LOGGER.info("addSended(" + login + ", " + s +" )");
        long timeStamp = Instant.now().getEpochSecond();
        Message msg = new Message(userDAO.getUsername(), activeUser, timeStamp, s);
        
        if(histories.get(login) == null)
            histories.put(login, new ArrayList<>());
        
//        if(histories.get(login).isEmpty() || chats.get(login) == null)
//            connectToUser();
        
        histories.get(login).add(msg);
        notifyOutSocket(msg, login);
    }
    
    public void connectToUser() {
        if(chats.containsKey(activeUser)) {
            SceneManager.getInstance().alertMsg(
                    "Aviso", "Conexão existe", "Usuário já conectado",
                    Alert.AlertType.INFORMATION
            );
            return;
        }
        
        LOGGER.info("connectToUser");
        OnlineUser user = OnlineUserDAO.getInstance().getUser(activeUser);
            try {
                Socket soc = new Socket(user.getIp(), user.getPort());
                Chat c = new Chat(soc, userDAO.getUsername());
                chats.put(activeUser, c);
                c.start();
                LOGGER.info("Novo chat com " + activeUser);
            } catch (IOException ex) {
                Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void addRecived(Message msg) throws DaoException {
        LOGGER.log(Level.INFO, "Message recived: {0}", msg);
        if(msg == null || !msg.isValid())
            throw new InvalidMessageException("Recived invalid message");
        
        if(!msg.getReciver().equals(userDAO.getUsername()))
            throw new InvalidReciverException(msg.getReciver(), userDAO.getUsername());
        
        String login = msg.getSender();
        if(histories.get(login) == null)
            histories.put(login, new ArrayList<>());
        
        histories.get(login).add(msg);
        LOGGER.info("Adiconado a historia");
        
        if(msg.getSender().equals(activeUser)) {
            notifyChatScene(msg);
            LOGGER.info("csc notificado");
        } else {
            chatScene.adcValueForUser(msg.getSender());
            LOGGER.info("incrementado o valor");
        }
    }

    public void setChatScene(ChatSceneController chatScene) {
        this.chatScene = chatScene;
    }
    
    public void notifyChatScene(Message msg) {
        chatScene.addMessage(msg.getSender(), msg.getText());
    }
    
    public void notifyOutSocket(Message msg, String login) {
//        LOGGER.info("notifyOutSocket(" + msg + ", " + login + "");
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
        OnlineUserDAO.getInstance().refreshUsers();
    }

    public void closeChats() {
        chats.forEach((String s, Chat c) -> {
            c.interrupt();
        });
    }
    
}
