package com.company.server.service;

import com.company.common.ClientType;
import com.company.server.processor.UserChatThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServerService {

    private static final UserService userService = new UserServiceImpl();
    private final int port;

    public ChatServerService(int port) {
        this.port = port;
    }


    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection established");
                var newUser = new UserChatThread(socket, this);
                userService.addUserConnection(newUser);
                newUser.start();
            }

        } catch (IOException ex) {
            System.err.println("Server error " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void broadcast(String message, UserChatThread excludedUser) {
        userService.findAllReaderUsers()
                .stream()
                .filter(userChatThread -> userChatThread != excludedUser)
                .forEach(userChatThread -> userChatThread.sendMessage(message));
    }

    public void updateUserConnection(String username, UserChatThread userChatThread, ClientType clientType) {
        userService.updateUserConnection(username, userChatThread, clientType);
    }

    public void removeUser(String username) {
        userService.removeUser(username);
        System.out.println("User " + username + " quit the chat");
    }

    public void removeConnection(UserChatThread userChatThread) {
        userService.removeConnection(userChatThread);
    }


}
