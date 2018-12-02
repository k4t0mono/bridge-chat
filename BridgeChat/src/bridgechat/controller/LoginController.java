/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat.controller;

import bridgechat.util.SceneManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void logar(){
        Scene cena2 = SceneManager.getInstance().loadScene("ChatScene");
        SceneManager.getInstance().setPrimaryScene(cena2);
    }
    
    
}
