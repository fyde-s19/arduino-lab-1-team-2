package assignment5;

import java.io.FileInputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//    	FXMLLoader loader = new FXMLLoader();
//     	String fxmlDocPath = "src/assignment5/UserInput.fxml";
//    	FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
    	
    
    	AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("UserInput.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Critter Input");
		primaryStage.show();
    	
    	//AnchorPane root = (AnchorPane) loader.load(fxmlStream);
    	
    }
}
