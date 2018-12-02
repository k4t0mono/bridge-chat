/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgechat.controller;

import bridgechat.backend.Node;
import bridgechat.backend.chat.Message;
import bridgechat.dao.MessageDAO;
import bridgechat.dao.OnlineUserDAO;
import bridgechat.dao.UserDAO;
import bridgechat.util.SceneManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ChatSceneController implements Initializable  {

    int indexTxtArea = 0;
    
    @FXML
    private JFXTextArea msgArea;
    @FXML
    private JFXTextArea txtArea;

    @FXML
    private SplitPane splitPaneCenter;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private JFXButton btnSendMsg;
    
    @FXML
    private TableView<Person> tvUsers;
        @FXML
        private TableColumn<Person, String> tableName;
        @FXML
        private TableColumn<Person, Integer> tableAmountMsg;
    
    ObservableList<Person> dateTable;
    
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    private OnlineUserDAO onlineUserDAO;
        
    @Override
    public void initialize(URL url, ResourceBundle rb){
        SceneManager.getInstance().getPrimaryStage().setResizable(true);
        
        this.messageDAO = MessageDAO.getInstace();
        this.userDAO = UserDAO.getInstance();
        messageDAO.setChatScene(this);
        this.onlineUserDAO = OnlineUserDAO.getInstance();
        onlineUserDAO.setChatSceneController(this);
        
        splitPaneCenter.setDividerPositions(0.25);
        
        // modelo tabeela
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableAmountMsg.setCellValueFactory(new PropertyValueFactory<>("amountMsg"));
    }    
    
    public void insertTextArea(String txt) {
        txtArea.appendText(txt + "\n\n");
    }
    
    public void insertMessage(Message msg) {
        insertTextArea(msg.getSender() + ": " + msg.getText());
    }
    
    public void adcTableValue(List<String> list){
        ArrayList<Person> people = new ArrayList<>();
        list.forEach((username) -> {
            people.add(new Person(username, 0));
        });
        
        dateTable = FXCollections.observableArrayList(people);
        tvUsers.setItems(dateTable);
    }
    
    private void adcTableValue(String name, Integer amountMsg){
        dateTable.add(new Person(name, amountMsg));
        tvUsers.setItems(dateTable);
    }

    private void adcValueForUser(String name){
        ObservableList<Person> listTable = tvUsers.getItems();
        
        for (Person p : listTable){
            if (p.getName().equalsIgnoreCase(name)) {
                p.setAmountMsg(p.getAmountMsg() + 1);
                return;
            }
        }
    }
    
    private void adcValueForUser(String name, Integer amountMsg){
        ObservableList<Person> listTable = tvUsers.getItems();
        
        for (Person p : listTable){
            if (p.getName().equalsIgnoreCase(name)) {
                p.setAmountMsg(p.getAmountMsg() + amountMsg);
                return;
            }
        }
    }
    
    @FXML
    private void clickUser(){  
        Person selected = (Person) tvUsers.getSelectionModel().getSelectedItem();
        System.out.println(selected.getName());
    }
    
    @FXML
    private void sendOnClick() {        
        if (!msgArea.getText().isEmpty()){
            messageDAO.addSended(msgArea.getText());
            insertTextArea(userDAO.getUsername() + ": " + msgArea.getText());
            msgArea.clear();
        }
    }
    
    public static class Person{
        SimpleStringProperty name;
        SimpleIntegerProperty amountMsg;
        
        public Person(String name, Integer amountMsg){
            this.name = new SimpleStringProperty(name);
            this.amountMsg = new SimpleIntegerProperty(amountMsg);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getAmountMsg() {
            return amountMsg.get();
        }

        public SimpleIntegerProperty amountMsgProperty() {
            return amountMsg;
        }

        public void setAmountMsg(int idade) {
            this.amountMsg.set(idade);
        }
        
    }
}
