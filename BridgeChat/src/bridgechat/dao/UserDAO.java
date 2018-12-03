package bridgechat.dao;

import bridgechat.backend.Node;

public class UserDAO {
    
    private static UserDAO instance = null;
    
    private String username;
    private String trackerAddr;
    
    private UserDAO() {}

    public static UserDAO getInstance() {
        if(instance == null)
            instance = new UserDAO();
        
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrackerAddr() {
        return trackerAddr;
    }

    public void setTrackerAddr(String trackerAddr) {
        this.trackerAddr = trackerAddr;
        Node.setTrackerAddr(trackerAddr);
    }
    
}
