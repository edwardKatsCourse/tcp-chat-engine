package com.company.client.hook;

import java.io.IOException;
import java.net.Socket;

public class SocketCloseShutdownHook extends Thread {

    private final Socket socket;

    public SocketCloseShutdownHook(Socket socket) {
        this.setDaemon(true);
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket connection: " + e.getMessage());
        }
    }
}
