package com.company.client.service;

import com.company.client.hook.SocketCloseShutdownHook;
import com.company.client.type.AbstractClient;
import com.company.common.ClientType;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChatService {

    private final String hostname;
    private final int port;
    private final ClientType clientType;

    public ClientChatService(String hostname, int port, ClientType clientType) {
        this.hostname = hostname;
        this.port = port;
        this.clientType = clientType;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to the chat server");

            Runtime.getRuntime().addShutdownHook(new SocketCloseShutdownHook(socket));

            AbstractClient client = AbstractClient.builder()
                    .clientType(clientType)
                    .socket(socket)
                    .build();

            client.startClient();


        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
