package bridgechat;

import bridgechat.backend.Node;
import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BridgeChat extends Application {

    private static Node node;
    
    @Override
    public void start(Stage stage) throws Exception {
        node = new Node();
        
        Scene cena = SceneManager.getInstance().loadScene("LoginScene");
        if (cena != null) {
            SceneManager.getInstance().getPrimaryStage().centerOnScreen();
            
            SceneManager.getInstance().getPrimaryStage().setResizable(false);
            SceneManager.getInstance().setPrimaryScene(cena);
        }
    }

    public static void startNode() {
        node.start();
    }
    
    public static void closeNode() {
        node.interrupt();
        try {
            Socket s = new Socket("localhost", Node.getPORT());
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(BridgeChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
