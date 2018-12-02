/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat;

import bridgechat.backend.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import bridgechat.util.SceneManager;

/**
 *
 * @author Gabriel
 */
public class BridgeChat extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        new Node().start();
        
        Scene cena = SceneManager.getInstance().loadScene("LoginScene");
        
        if (cena != null) {
            SceneManager.getInstance().getPrimaryStage().centerOnScreen();
            
            SceneManager.getInstance().getPrimaryStage().setResizable(false);
            SceneManager.getInstance().setPrimaryScene(cena);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
