package org.hadifaraz.server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    String userName;
    Socket socket;

    public ServerThread(String userName, Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.dis = new DataInputStream(socket.getInputStream());
        this.userName = userName;
    }

    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();

                server.sendMsg(userName, message);
            }
        } catch (IOException e) {
            System.out.println("Do something");
        } finally {
            server.removeConnection(userName);
        }
    }
}
