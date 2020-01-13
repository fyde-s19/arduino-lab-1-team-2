package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//import java.util.Observable;
//import java.util.Observer;
//
//import javafx.application.Application;


public class ChatClientMain {
	private String serverName;
	private static final int port = 8989;
	private Socket socket;
	private String UserName;
	
	private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
	
	public static Set<String> user = new HashSet<>();
	private ArrayList<MessageListener> messageListeners = new ArrayList<>();

	public ChatClientMain(String hostname){
		serverName = hostname;
		messageListeners.add(new MessageListener() { 
			@Override 
			public void receive(String fromUser, String msg) {
				System.out.println("You got a message from " + fromUser + " ===>" + msg);
			}
		});
		
		
		
	}

	public void go(){
		try{
			socket = new Socket(serverName, port);
			//System.out.println("Client port is " + socket.getLocalPort());
            
            //new WriteThread(socket, this).start();
			
			try {
				serverIn = socket.getInputStream();
				serverOut = socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	void setUserName(String username){
		this.UserName = username;
	}

	public String getUserName() {
		return UserName;
	}

	public boolean login(String login) {
		String cmd = "login " + login + "\n";
        try {
			serverOut.write(cmd.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String response = null;
		try {
			response = bufferedIn.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if ("ok login ".equalsIgnoreCase(response)) {
            Thread t = new ReadThread(socket, this, bufferedIn);
            t.start();
            return true;
        } else {
            return false;
        }
	}
	
	
	public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

	public void message(String toUser, String currentMessage, String fromUser) {

		String cmd = "msg " + toUser + " "  + fromUser +  " " + currentMessage + "\n";
        try {
			serverOut.write(cmd.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
}
