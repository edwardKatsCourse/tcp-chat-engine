package com.company.client.type;

import com.company.client.console.EntryUsernameSupplier;
import com.company.client.type.types.AdminClient;
import com.company.client.type.types.ReadClient;
import com.company.client.type.types.WriteClient;
import com.company.common.ClientType;
import com.company.common.PayloadDTO;

import java.io.*;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class AbstractClient {
    private static final Supplier<String> usernameSupplier = new EntryUsernameSupplier();


    private final ObjectOutputStream requestSender;
    private final ObjectInputStream responseReceiver;
    private final String username;

    private final ClientType clientType;

    public AbstractClient(OutputStream output, InputStream input, ClientType clientType) {

        try {
            this.clientType = clientType;

            this.requestSender = new ObjectOutputStream(output);
            this.username = usernameSupplier.get();
            this.responseReceiver = new ObjectInputStream(input);

            sendRequest(username);
            System.out.printf("Client [%s] connected with [%s] client type\n", username, clientType);
        } catch (IOException e) {
            System.err.println("Error on creating ObjectInput or ObjectOutput Streams");
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    public abstract void startClient();


    public void sendRequest(String message) {
        PayloadDTO payload = new PayloadDTO(clientType, message);
        try {
            requestSender.writeObject(payload);
            requestSender.flush();
        } catch (IOException e) {
            System.err.println("Error on sending request object: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String readResponse() {
        try {
            return responseReceiver.readUTF();
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }


    public static class ClientBuilder {
        private ClientType clientType;
        private Socket socket;

        private ClientBuilder() {
        }

        public ClientBuilder socket(Socket socket) {
            this.socket = socket;
            return this;
        }

        public ClientBuilder clientType(ClientType clientType) {
            this.clientType = clientType;
            return this;
        }


        public AbstractClient build() {

            InputStream inputStream = getSocketInputStream();
            OutputStream outputStream = getSocketOutputStream();

            return switch (clientType) {
                case READ -> new ReadClient(outputStream, inputStream, clientType);
                case WRITE -> new WriteClient(outputStream, inputStream, clientType);
                case ADMIN -> new AdminClient(outputStream, inputStream, clientType);
            };
        }

        private OutputStream getSocketOutputStream() {
            try {
                return socket.getOutputStream();
            } catch (IOException e) {
                System.err.println("Error on querying socket output stream: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private InputStream getSocketInputStream() {
            try {
                return socket.getInputStream();
            } catch (IOException e) {
                System.err.println("Error on querying socket input stream: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }
}
