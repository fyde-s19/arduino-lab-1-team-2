package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadMessages extends Thread{
	private BufferedReader bufferedIn;
	ArrayList<MessageListener> messageListeners;
	
	public ReadMessages(BufferedReader b, ArrayList<MessageListener> messageListeners) {
		bufferedIn = b;
		this.messageListeners = messageListeners;
	}
	
	public void run() {
		readMessage();
    }

	private void readMessage() {
		String msg;
		try {
			while ((msg = bufferedIn.readLine()) != null) {
				String[] msgCut = msg.split(" ", 3);
				if (msgCut != null && msgCut.length > 0) {
                    String cmd = msgCut[0];
                    if ("msg".equalsIgnoreCase(cmd)) {
                    	handleMessage(msgCut);
                    }
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void handleMessage(String[] msgCut) {
		String user = msgCut[1];
        String msg = msgCut[2];

        for(MessageListener listener : messageListeners) {
            listener.receive(user, msg);
        }
		
	}


}
