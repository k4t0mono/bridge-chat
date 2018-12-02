package bridgechat.controller;

import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;


public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtLogin;
    @FXML
    private JFXTextField txtPass;
    private UserDAO dao;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = UserDAO.getInstance();
    }    
    
    public void logar(){
        dao.setUsername(txtLogin.getText());
        dao.setPassword(txtPass.getText());
        bridgechat.BridgeChat.startNode();
                
        Scene cena2 = SceneManager.getInstance().loadScene("ChatScene");
        SceneManager.getInstance().setPrimaryScene(cena2);
    }
    
    
}
