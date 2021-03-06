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
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Logger;
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
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    private OnlineUserDAO onlineUserDAO;
    private String activeUser;
    private static final Logger LOGGER = Logger.getLogger("CSC");
    
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
    
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private JFXButton btnConnect;
        
    @Override
    public void initialize(URL url, ResourceBundle rb){
        SceneManager.getInstance().getPrimaryStage().setResizable(true);
        
        this.messageDAO = MessageDAO.getInstace();
        this.userDAO = UserDAO.getInstance();
        messageDAO.setChatScene(this);
        this.onlineUserDAO = OnlineUserDAO.getInstance();
        onlineUserDAO.setChatSceneController(this);
        activeUser = "";
        
        splitPaneCenter.setDividerPositions(0.25);
        
        // modelo tabeela
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableAmountMsg.setCellValueFactory(new PropertyValueFactory<>("amountMsg"));
    }    
    
    public void adcTableValue(List<String> list){
        ArrayList<Person> people = new ArrayList<>();
        list.forEach((username) -> {
            people.add(new Person(username, 0));
        });
        
        dateTable = FXCollections.observableArrayList(people);
        tvUsers.setItems(dateTable);
    }
    
    public void adcTableValue(String name, Integer amountMsg){
        dateTable.add(new Person(name, amountMsg));
        tvUsers.setItems(dateTable);
    }

    public void adcValueForUser(String name){
        ObservableList<Person> listTable = tvUsers.getItems();
        
        for (Person p : listTable){
            if (p.getName().equalsIgnoreCase(name)) {
                p.setAmountMsg(p.getAmountMsg() + 1);
                return;
            }
        }
    }
    
    public void adcValueForUser(String name, Integer amountMsg){
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
        String s = selected.getName();
        if(s.equals(activeUser))
            return;
        
        activeUser = s;
        messageDAO.setActiveUser(activeUser);
        
        txtArea.clear();
        selected.setAmountMsg(0);
        Iterator itr = messageDAO.getHistoryIterator(activeUser);
        while(itr.hasNext()) {
            Message m = (Message) itr.next();
            addMessage(m.getSender(), m.getText());
        }
    }
    
    public void addMessage(String user, String txt) {
        txtArea.appendText(user + ": " + txt + "\n\n");
    }
    
    @FXML
    private void sendOnClick() {        
        if (!msgArea.getText().isEmpty()){
            LOGGER.info("Menssage escrita");
            messageDAO.addSended(activeUser, msgArea.getText());
            addMessage(userDAO.getUsername(), msgArea.getText());
            msgArea.clear();
        }
    }
    
    @FXML
    private void refreshOnClick() {
        onlineUserDAO.refreshUsers();
    }
    
    @FXML
    private void connectOnClick() {
        messageDAO.connectToUser();
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

        public void setAmountMsg(int num) {
            this.amountMsg.set(num);
        }
        
    }
}
