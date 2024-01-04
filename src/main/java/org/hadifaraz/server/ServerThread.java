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
        this.userName = userName;

        start();
    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            //noinspection InfiniteLoopStatement
            while (true) {
                String message = dis.readUTF();
                System.out.println("Received message.");

                server.sendMsg(userName, message);
                System.out.println("Sent message to all clients.");
            }
        } catch (EOFException ignored) {
        } catch (IOException e) {
            System.out.println("IOException has occurred.");

            Misc.promptErr(e);
        }  finally {
            server.removeConnection(userName);
        }
    }
}
