/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat;

import bridgechat.backend.Node;
import bridgechat.backend.chat.Chat;
import bridgechat.backend.chat.Message;
import bridgechat.dao.MessageDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import bridgechat.util.SceneManager;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Gabriel
 */
public class BridgeChat extends Application {

    private static Node node;
    
    @Override
    public void start(Stage stage) throws Exception {
//        Scanner scan = new Scanner(System.in);
//        System.out.print("Username: ");
//        String username = scan.nextLine();
//        
//        int op = scan.nextInt();
//        
//        if(op == 1) {
//            String addr = "localhost";
//            
//            System.out.print("Nick: ");
//            scan.nextLine();
//            MessageDAO.getInstace().setChatUsername(scan.nextLine());
//
//            System.out.print("Port: ");
//            int port = scan.nextInt();
//
//            Socket s = new Socket(addr, port);
//            Chat cs = new Chat(s, username);
//            cs.start();
//        }
        node = new Node();
//        node.start();
        
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
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
