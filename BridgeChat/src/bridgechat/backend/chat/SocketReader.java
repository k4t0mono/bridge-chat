package bridgechat.backend.chat;

import bridgechat.dao.exception.DaoException;
import bridgechat.dao.MessageDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketReader extends Thread {

    private final BufferedReader in;
    
    public SocketReader(BufferedReader in) {
        super();
        
        this.in = in;
    }
    
    @Override
    public void run() {
        MessageDAO dao = MessageDAO.getInstace();
        Gson gson = new GsonBuilder().create();
        
        try {
            String s = in.readLine();
            while(s != null) {
                try {
                    Message msg = gson.fromJson(s, Message.class);
                    dao.addRecived(msg);
                } catch (DaoException | JsonParseException ex) {
                    System.out.println("ex: " + ex.getMessage());
                }
                
                s = in.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(SocketReader.class.getName()).log(Level.FINER, "SocketReader ended");
    }
    
}
