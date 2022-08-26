package com.company.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private Socket socket;
    private ChatClient chatClient;
    private PrintWriter writer;

    public WriteThread(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("\nEnter your name: ");
        String userName = new Scanner(System.in).nextLine();

        chatClient.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            System.out.printf("[%s]: \n", userName);
            text = new Scanner(System.in).nextLine();
            writer.println(text);
        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }

    }
}
