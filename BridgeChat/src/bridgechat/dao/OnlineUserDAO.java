package bridgechat.dao;

import bridgechat.backend.Node;
import bridgechat.backend.tracker.OnlineUser;
import bridgechat.controller.ChatSceneController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnlineUserDAO {
    
    private static List<OnlineUser> users;
    private static OnlineUserDAO instance = null;
    private ChatSceneController csc;
    
    private OnlineUserDAO() {
        users = new ArrayList<>();
    }

    public static OnlineUserDAO getInstance() {
        if(instance == null)
            instance = new OnlineUserDAO();
        
        return instance;
    }
    
    public void setChatSceneController(ChatSceneController csc) {
        this.csc = csc;
    }
    
    public void setUsers(List<OnlineUser> list) {
        users.clear();
        users.addAll(list);
        notifyController();
    }
    
    public void setUsers(OnlineUser[] list) {
        users.clear();
        users.addAll(Arrays.asList(list));
        notifyController();
    }
    
    public void addUser(OnlineUser user) {
        users.add(user);
    }
    
    public List<String> getUsersLogin() {
        ArrayList<String> l = new ArrayList<>();
        users.forEach((u) -> {
            l.add(u.getLogin());
        });
        
        return l;
    }
    
    public OnlineUser getUser(String username) {
        for(OnlineUser user : users) {
            if(user.getLogin().equals(username))
                return user;
        }
        
        return null;
    }
    
    public void notifyController() {
        csc.adcTableValue(getUsersLogin());
    }
    
    public void refreshUsers() {
        try {
            setUsers(Node.getOnlines());
        } catch (IOException ex) {
            Logger.getLogger(OnlineUserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
