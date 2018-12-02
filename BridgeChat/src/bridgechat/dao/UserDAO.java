package bridgechat.dao;

public class UserDAO {
    
    private static UserDAO instance = null;
    
    private String username;
    private String password;
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
