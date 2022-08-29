package com.company.client.type.types;

import com.company.client.type.AbstractClient;
import com.company.common.ClientType;

import java.io.InputStream;
import java.io.OutputStream;

public class ReadClient extends AbstractClient {


    public ReadClient(OutputStream output, InputStream input, ClientType clientType) {
        super(output, input, clientType);
    }

    public void startClient() {
        while (true) {
            String response = super.readResponse();
            System.out.println(response);
        }
    }
}
