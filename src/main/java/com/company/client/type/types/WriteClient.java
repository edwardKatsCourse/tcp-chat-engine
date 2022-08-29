package com.company.client.type.types;

import com.company.client.type.AbstractClient;
import com.company.client.utils.ConsoleUtils;
import com.company.common.ClientType;

import java.io.InputStream;
import java.io.OutputStream;

public class WriteClient extends AbstractClient {

    public WriteClient(OutputStream output, InputStream input, ClientType clientType) {
        super(output, input, clientType);
    }


    public void startClient() {
        String message;

        do {
            String caption = String.format("[%s]: ", super.getUsername());
            message = ConsoleUtils.getInput(caption, true);
            super.sendRequest(message);
        } while (!message.equals("bye"));
    }
}
