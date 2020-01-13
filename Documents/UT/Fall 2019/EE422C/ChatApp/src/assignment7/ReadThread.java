package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.scene.control.TextArea;

public class ReadThread extends Thread{
    private BufferedReader reader;
    private Socket socket;
    private ChatClientMain client;
    private ClientUICtrl ctrl;

    public ReadThread(Socket socket, ChatClientMain client, BufferedReader bufferedIn){
        this.socket = socket;
        this.client = client;
        reader = bufferedIn;
        
    }

    public void run(){
    	String msg;
        while(true){
            try {
            	while ((msg = reader.readLine()) != null) {
    				String[] msgCut = msg.split(" ", 3);
    				if (msgCut != null && msgCut.length > 0) {
                        String cmd = msgCut[0];
                        if ("msg".equalsIgnoreCase(cmd)) {
                        	handleMessage(msgCut);
                        }
    				}
    			}
            	
            	// TextArea chat = ctrl.Chat;
            	//chat.appendText(reader.readLine() + "\n");
//                System.out.println("\n" + reader.readLine());
//                if (client.getUserName() != null) 
//                    System.out.print(client.getUserName() + ":   ");
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

	private void handleMessage(String[] msgCut) {
		String fromUser = msgCut[1];
        String msg = msgCut[2];
        
        //ctrl = Main.loader.getController();
        //ctrl.Chat.appendText("\n" + fromUser + ":	" + msg);

//        for(MessageListener listener : messageListeners) {
//            listener.receive(user, msg);
//        }
		
	}

}
