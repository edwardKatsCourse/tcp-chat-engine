package com.company.client.type.types;

import com.company.client.type.AbstractClient;
import com.company.common.ClientType;

import java.io.InputStream;
import java.io.OutputStream;

public class AdminClient extends AbstractClient {


    public AdminClient(OutputStream output, InputStream input, ClientType clientType) {
        super(output, input, clientType);
    }

    public void startClient() {

    }
}
