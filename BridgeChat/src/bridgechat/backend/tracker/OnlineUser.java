package bridgechat.backend.tracker;

public class OnlineUser {
    
    private String login;
    private String ip;
    private int port;

    public OnlineUser(String login, String ip, int port) {
        this.login = login;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "OnlineUser{" + "login=" + login + ", ip=" + ip + ", port=" + port + '}';
    }
    
    public String getLogin() {
        return login;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
    
}
