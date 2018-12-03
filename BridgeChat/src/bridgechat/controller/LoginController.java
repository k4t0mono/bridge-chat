package bridgechat.controller;

import bridgechat.backend.Node;
import bridgechat.backend.tracker.exception.SameUserException;
import bridgechat.dao.OnlineUserDAO;
import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;


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
        
        Node.setup_ssl();
        try {
            Node.register_user(dao.getUsername());
            Node.broadcast_ip(1);
            
            bridgechat.BridgeChat.startNode();
            
            Scene cena2 = SceneManager.getInstance().loadScene("ChatScene");
            SceneManager.getInstance().setPrimaryScene(cena2);
            OnlineUserDAO.getInstance().setUsers(Node.getOnlines());
        } catch (IOException ex) {
            SceneManager.getInstance().alertMsg(
                    "Aviso", "Tracker não encontrado", "Não foi encontrador um tracker nesse endereço.",
                    Alert.AlertType.ERROR
            );
        } catch (SameUserException ex) {
            SceneManager.getInstance().alertMsg(
                    "Aviso", "Usário já existe", "Escolha outro nome de usuário",
                    Alert.AlertType.WARNING
            );
        }
    }
    
    
}
