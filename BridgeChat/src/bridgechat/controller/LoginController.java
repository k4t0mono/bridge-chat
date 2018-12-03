package bridgechat.controller;

import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;


public class LoginController implements Initializable {

    private UserDAO dao;
    @FXML
    private JFXTextField txtLogin;
    @FXML
    private JFXButton btnLogar;
    @FXML
    private JFXTextField txtTrackerAddr;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = UserDAO.getInstance();
    }    
    
    @FXML
    public void logar(){
        dao.setUsername(txtLogin.getText());
        
        if(txtTrackerAddr.getText().isEmpty()) 
            dao.setTrackerAddr("127.0.0.1");
        else
            dao.setTrackerAddr(txtTrackerAddr.getText());
        
        bridgechat.BridgeChat.startNode();
                
        Scene cena2 = SceneManager.getInstance().loadScene("ChatScene");
        SceneManager.getInstance().setPrimaryScene(cena2);
    }
    
    
}
