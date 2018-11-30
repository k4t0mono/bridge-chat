package bridgechat.backend.chat;

public class Message {
    
    private final String sender;
    private final String reciver;
    private final long time;
    private final String text;

    public Message(String sender, String reciver, long time, String text) {
        this.sender = sender;
        this.reciver = reciver;
        this.time = time;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
    
}
