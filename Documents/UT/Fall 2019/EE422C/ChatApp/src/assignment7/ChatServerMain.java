package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.fxml.FXMLLoader;

public class ChatServerMain {
	
	private static final int port = 8989;
	public Set<String> user = new HashSet<>();
	public Set<Users> thread = new HashSet<>();
	
	public static ArrayList<ChatClientMain> clients = new ArrayList<>();
	public static Map<String,FXMLLoader> loader = new HashMap<>();

	
	public static void main(String[] args) {
		ChatServerMain server = new ChatServerMain();
		server.go();
	}

	public void go() {
		try(ServerSocket serverSock = new ServerSocket(port)) {
			while(true) {
				System.out.println("About to accept client connection...");
				Socket clientSock = serverSock.accept();
				System.out.println("Accepted connection from " + clientSock);
				Users newUser = new Users(clientSock, this);
				thread.add(newUser);
				newUser.start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void broadcast(String serverMessage, Set<Users> users, String from) {
		for (Users u : users) {
			//if (u != users)
				u.send(serverMessage,from);
		}
		
	}

	public void removeUser(String userName, Users users) {
		boolean removed = user.remove(userName);
        if (removed) {
            thread.remove(users);
            System.out.println(userName + " quitted");
        }
	}
	
	public Set<Users> getUsers() {return thread;}


}
