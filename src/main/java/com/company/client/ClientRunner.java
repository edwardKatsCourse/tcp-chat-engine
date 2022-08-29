package com.company.client;

import com.company.client.console.ClientTypeChoosingSupplier;
import com.company.client.service.ClientChatService;
import com.company.common.ClientType;

import java.util.function.Supplier;

public class ClientRunner {

    private static final Supplier<ClientType> clientTypeSupplier = new ClientTypeChoosingSupplier();

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;


        var clientType = clientTypeSupplier.get();

        ClientChatService client = new ClientChatService(hostname, port, clientType);
        client.execute();
    }
}
