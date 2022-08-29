package com.company.server;

import com.company.server.service.ChatServerService;

public class ChatServerRunner {

    public static void main(String[] args) {
        int port = 8080;
        ChatServerService server = new ChatServerService(port);
        server.execute();
    }
}
