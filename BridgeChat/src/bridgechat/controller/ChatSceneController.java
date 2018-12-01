/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat.controller;

import bridgechat.dao.MessageDAO;
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
    private MessageDAO dao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dao = MessageDAO.getInstace();
        dao.setChatScene(this);
    }    
    
    public void sendOnClick(){
        if (!msgArea.getText().isEmpty()){
            dao.addSendedMessage(msgArea.getText());
            insertTextArea(msgArea.getText());
            msgArea.clear();
        }
    }
    
    public void insertTextArea(String msg){
        txtArea.appendText("FULANO: " + msg + '\n');
    }
}
