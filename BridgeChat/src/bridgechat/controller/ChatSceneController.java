/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import bridgechat.util.SceneManager;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class ChatSceneController implements Initializable {

    int indexTxtArea = 0;
    
    @FXML
    private JFXButton btnSendMsg;
    @FXML
    private JFXTextField txtFMsg;
    @FXML
    private JFXTextArea txtArea;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void sendOnClick(){
        insertTextArea(txtFMsg.getText());
        txtFMsg.clear();
    }
    
    public void insertTextArea(String msg){
        txtArea.appendText("FULANO: " + msg + '\n');
    }
    
    
}
