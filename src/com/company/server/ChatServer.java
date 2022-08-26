package com.company.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();


    public ChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 8080;
        ChatServer server = new ChatServer(port);
        server.execute();
    }


    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                var newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }

        } catch (IOException ex) {
            System.err.println("Server error " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void broadcast(String message, UserThread excludedUser) {
        userThreads.stream()
                .filter(userThread -> userThread != excludedUser)
                .forEach(userThread -> userThread.sendMessage(message));
    }

    public void addUserName(String userName) {
        userNames.add(userName);
    }

    public void removeUser(String userName, UserThread userThread) {
        boolean isRemoved = userNames.remove(userName);
        if (isRemoved) {
            userThreads.remove(userThread);
            System.out.println("User " + userName + " quit the chat");
        }
    }


}
