package com.company.server.processor;

import com.company.common.ClientType;
import com.company.common.PayloadDTO;
import com.company.server.service.ChatServerService;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public class UserChatThread extends Thread {
    private final Socket socket;
    private final ChatServerService server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String username;
    private ClientType clientType;

    public UserChatThread(Socket socket, ChatServerService server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            PayloadDTO usernameRequest = listenRequest();
            this.username = usernameRequest.message();
            this.clientType = usernameRequest.clientType();

            server.updateUserConnection(this.username, this, usernameRequest.clientType());


            String serverMessage = String.format("New user [%s] connected with client [%s]", username, clientType);
            server.broadcast(serverMessage, this);

            PayloadDTO clientRequest;

            do {
                clientRequest = listenRequest();
                serverMessage = String.format("[%s]: %s", this.username, clientRequest.message());
                server.broadcast(serverMessage, this);
            } while (!clientRequest.message().equals("bye"));

            server.removeUser(this.username);
            socket.close();

            serverMessage = String.format("%s has quit", this.username);
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public PayloadDTO listenRequest() {
        try {
            return (PayloadDTO) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred in userThread | listenRequest(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (SocketException e) {
            System.out.printf("Client [%s] with UserThread [%s] disconnected, evicting.. \n", username, this);

            server.removeConnection(this);

        } catch (IOException e) {
            System.err.println("Error occurred in userThread | sendMessage(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChatThread that = (UserChatThread) o;
        return socket.equals(that.socket) && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, username);
    }
}
