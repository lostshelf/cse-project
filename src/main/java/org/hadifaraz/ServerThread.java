package org.hadifaraz;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    Server server;
    Socket socket;
    String userName;

    public ServerThread(String userName, Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.userName = userName;
    }

    public void start() {

    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while (true) {
                String message = dis.readUTF();

                server.sendMsg(message);
            }
        } catch (IOException e) {
            System.out.println("Do something");
        } finally {
            server.removeConnection(userName);
        }
    }
}
