/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat.controller;

import bridgechat.backend.Node;
import bridgechat.backend.chat.Message;
import bridgechat.dao.MessageDAO;
import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ChatSceneController implements Initializable  {

    int indexTxtArea = 0;
    
//    @FXML
//    private JFXTextField txtFMsg;
    
    @FXML
    private JFXTextArea msgArea;
    @FXML
    private JFXTextArea txtArea;
    private MessageDAO messageDAO;
    private UserDAO userDAO;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SceneManager.getInstance().getPrimaryStage().setResizable(true);
        this.messageDAO = MessageDAO.getInstace();
        this.userDAO = UserDAO.getInstance();
        messageDAO.setChatScene(this);
    }    
    
    public void sendOnClick(){
        if (!msgArea.getText().isEmpty()){
            messageDAO.addSended(msgArea.getText());
            insertTextArea(userDAO.getUsername() + ": " + msgArea.getText());
            msgArea.clear();
        }
    }
    
    public void insertTextArea(String txt) {
        txtArea.appendText(txt + "\n\n");
    }
    
    public void insertMessage(Message msg) {
        insertTextArea(msg.getSender() + ": " + msg.getText());
    }
}
