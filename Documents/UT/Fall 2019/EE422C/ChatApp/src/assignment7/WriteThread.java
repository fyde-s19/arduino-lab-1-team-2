package assignment7;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClientMain client;
    


    public WriteThread(Socket socket, ChatClientMain client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        //Console console = System.console();
        /*
        System.out.print("\nEnter your name: ");
        String userName = s.nextLine();
        client.setUserName(userName);
         */
        Scanner s = new Scanner(System.in);
        String userName = client.getUserName();
        writer.println(userName);
        
        String text;
        do {
        	System.out.print(userName + ":   ");
        	text = s.nextLine();
            writer.println(text);
        } while (!text.equals("quit"));

        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

