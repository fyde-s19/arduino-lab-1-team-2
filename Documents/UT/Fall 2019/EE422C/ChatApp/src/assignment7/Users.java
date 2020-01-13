package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

public class Users extends Thread{
	private Socket clientSocket;
	private ChatServerMain server;
	private PrintWriter writer;
	private String userName;
	private OutputStream outputStream;

	

	public Users(Socket sock, ChatServerMain chatServerMain) {
		clientSocket = sock;
		server = chatServerMain;
	}
	
	public void run() {
		try {
			handleClientSocket();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

	private void handleClientSocket() throws IOException, InterruptedException {
		InputStream inputStream = clientSocket.getInputStream();
		outputStream = clientSocket.getOutputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//		writer = new PrintWriter(outputStream, true);
		
//		String userName = reader.readLine();
//		server.addUser(userName);
//		
//		String serverMessage = "New connection by:   " + userName;
//		server.broadcast(serverMessage, this);
		
		String clientMessage;
		
		while((clientMessage = reader.readLine()) != null) {
			String[] messageCuts = clientMessage.split(" ", 4);
			
			if (messageCuts != null && messageCuts.length > 0) {
				String cmd = messageCuts[0];
				
				if("logoff".equalsIgnoreCase(cmd)) {
					handleLogOff();
					break;
				} else if("login".equalsIgnoreCase(cmd)) {
					handleLogin(outputStream, messageCuts);
				} else if ("msg".equalsIgnoreCase(cmd)) {
					handleMessage(messageCuts);
				}
				else {
					String msg = "unknown" + cmd + "command \n";
					outputStream.write(msg.getBytes());
				}
					
			}
//			serverMessage = userName + ":   " + clientMessage;
//			server.broadcast(serverMessage, this);
		}
		
		clientSocket.close();
		
//		serverMessage = userName + " has quitted.";
//		server.broadcast(serverMessage, this);
	}

	private void handleMessage(String[] m) {
		String sendTo = m[1];
		String from = m[2];
		String msg = m[3];
		Set<Users> users = server.getUsers();
		
		if (sendTo.equalsIgnoreCase("global")) {
			server.broadcast(msg, users,from);
		} else {
//			for (Users u : users) {
//				if (sendTo.equalsIgnoreCase(u.getUserName())) {
//					String outMsg = "msg " + userName + " " + msg + "\n";
//	                u.send(outMsg);
//				}
//			}
		}
	}

	private void handleLogOff() throws IOException {
		server.removeUser(userName, this);
		
		Set<Users> users = server.getUsers();
		
		String msg = userName + " went offline :( \n";
		
		for(Users u : users) {
			if (u != this){}
			//	u.send(msg);
		}
		
		clientSocket.close();
	}

	private void handleLogin(OutputStream outputStream, String[] messageCuts) {
		if (messageCuts.length == 2) {
			String login = messageCuts[1];
			
			String msg = "ok login \n";
			try {
				outputStream.write(msg.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			userName = login;
			System.out.println("User logged in successfully:   " + login);
			
			Set<Users> users = server.getUsers();
			
			// tell everyone about new connection
			for(Users u : users) {
				String msg2 = userName + " is online :) \n";
				
				if (u.userName != null && u != this){}
	//				u.send(msg2);
			}
			
			// tell new connector who else is on
			for(Users u : users) {
				String msg2 = u.getUserName() + " is online \n";
				
				if (u.userName != null && u != this){}
	//				send(msg2);
			}
		}
		
	}

	private String getUserName() {
		return userName;
	}

	public void send(String serverMessage, String fromUser) {
		// writer.println(serverMessage);
		
		if (userName != null) {
			//outputStream.write(serverMessage.getBytes());
			ClientUICtrl ctrl = ChatServerMain.loader.get(userName).getController();
			ctrl.Chat.appendText("\n" + fromUser + ":	" + serverMessage);
		}
		
	}

}
