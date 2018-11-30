package bridgechat.backend.tracker;

public class BroadcastMessage {
    
    private String ip;
    private int port;
    private int op;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getOp() {
        return op;
    }

    public BroadcastMessage(String ip, int port, int op) {
        this.ip = ip;
        this.port = port;
        this.op = op;
    }
    
}
