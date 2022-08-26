package com.company.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();


        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
