package assignment7;
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	public String newUser;
	
	
	public static void main(String[] args) {
		ChatClientMain client = new ChatClientMain("localhost");
		ChatServerMain.clients.add(client);
        client.go();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		/** UserName dialog Box **/
		String[] userName = {"Default Username"};
		boolean[] UserNameEntered = {false};
		GridPane gridPane = new GridPane();
		TextField textField = new TextField();
		
		gridPane.add(textField,0,10);
		Button bt = new Button("Enter Username");
		
		gridPane.add(bt,25,10);
		Stage stage1 = new Stage();
		stage1.setScene(new Scene(gridPane, 400, 100));
		stage1.show();
		
		bt.setOnAction(new EventHandler<ActionEvent>() { 
			public void handle(ActionEvent event) {
				System.out.println("Handled");
				String name = textField.getText();
				if(name.compareToIgnoreCase("") != 0 && name != null){
					userName[0] = textField.getText();
				}
				UserNameEntered[0] = true;
				stage1.close();
				
				
				ChatServerMain.clients.get(ChatServerMain.clients.size() - 1).login(userName[0]);
				newUser = userName[0];
				
				ChatClientMain.user.add(newUser);
				
				/**Chat Client GUI**/
				try {
					FXMLLoader fxmlLoader = new FXMLLoader();
					ChatServerMain.loader.put(newUser,fxmlLoader);
					AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("ClientUI.fxml"));
					Scene scene = new Scene(root,800,800);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					primaryStage.setScene(scene);
					primaryStage.setTitle("Chat");
					primaryStage.show();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		

		
	}

}
