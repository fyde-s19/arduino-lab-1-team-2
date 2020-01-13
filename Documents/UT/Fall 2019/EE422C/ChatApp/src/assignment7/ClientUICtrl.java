package assignment7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ClientUICtrl {
    String currentMessage = "";
    String GlobalChat = "";
    ArrayList<String> Users = new ArrayList<>();
    ArrayList<String> UserChat = new ArrayList<>();
    boolean Global;
    String CurrentUser;
    ChatClientMain client;

    @FXML
    public Button GlobalChatButton;
    @FXML
    public Button SendButton;
    @FXML
    public TextArea Chat;
    @FXML
    public ComboBox<String> DropDown;
    @FXML
    public Button DirectMessage;
    @FXML
    public TextField MessageBox;
    @FXML
    public Label ChatLabel;
    @FXML
    public Text username;
    @FXML
    public Text SendError;
    
    

    @FXML
    private void initialize(){
        /** Starting Client **/
        client = ChatServerMain.clients.get(ChatServerMain.clients.size() - 1);

        Global = true;
        CurrentUser = client.getUserName();
        client.setUserName(CurrentUser);
        
        username.setText("Username: " + CurrentUser);

        //Users.addAll(server.user);
//        ObservableList<String> availableChoices = FXCollections.observableArrayList(Users);
//        DropDown.setItems(availableChoices);
        ArrayList<String> choices = new ArrayList<String>();
    	
    	for (String s : ChatClientMain.user) {
    		if (s != CurrentUser)
    			choices.add(s);
    	}
    	
    	
    	ObservableList<String> availableChoices = FXCollections.observableArrayList(choices);
		DropDown.setItems(availableChoices);
    }

    @FXML
    private void sendButtonHandler(){
    	String msg = MessageBox.getText();
		if(msg.compareToIgnoreCase("") != 0 && msg != null){
	        currentMessage = MessageBox.getText();

	        
	        if(Global)
	        	client.message("global", currentMessage, this.CurrentUser);
	        //else														TODOOOOOO
	        //	client.message(<sendToUsername>, currentMessage);
	        
	        URL resource = getClass().getResource("ding.wav");
	    	AudioClip ding = new AudioClip(resource.toString());
	    	ding.play();
	    	
	    	Chat.appendText("\n" + CurrentUser + ":	" + currentMessage);
	        MessageBox.setText("");
		} else {
			//SendError.setText("No message written");
		}
    }

    @FXML
    private void DirectMessageHandler() {
        if(Global)
            GlobalChat = Chat.getText();
        else{
            UserChat.set(Users.indexOf(CurrentUser),Chat.getText());
        }
        String UserName = DropDown.getValue();
        CurrentUser = UserName;
        Global = false;
        String userChats = UserChat.get(Users.indexOf(UserName));
        Chat.setText(userChats);
        ChatLabel.setText("Direct Message: " + UserName);
    }

    @FXML
    private void GlobalChatHandler(){
        UserChat.set(Users.indexOf(CurrentUser),Chat.getText());
        Chat.setText(GlobalChat);
        CurrentUser = "";
        Global = true;
        ChatLabel.setText("Global Chat");
    }
    

    //possibly unneeded
    public void addUser(String newUser){
        Users.add(newUser);
        UserChat.add("");
        ObservableList<String> availableChoices = FXCollections.observableArrayList(Users);
        DropDown.setItems(availableChoices);
    }
    
    public TextArea getChat() {
		return Chat;
    }
    
    
    public void display(String u, String msg) {
    	Chat.appendText("\n" + u + ":	" + msg);
    }
    
    public void setClient(ChatClientMain c) {
    	client = c;
    }

    
}
