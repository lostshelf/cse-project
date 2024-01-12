package org.hadifaraz.server;

import java.io.*;
import java.net.*;

// Handles recieving messages from the Clients
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

        // Make sure the Thread actually starts
        // Spent WAY too much time trying to figure out why the Server wasn't recieving messages and it was all because I forgot to run this function
        start();
    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // Stop IntelliJ from whining about it
            //noinspection InfiniteLoopStatement
            while (true) {
                // Read message from the client
                String message = dis.readUTF();
                System.out.println("Received message.");

                // Send it to all the other clients
                server.sendMsg(userName, message);
                System.out.println("Sent message to all clients.");
            }
        } catch (EOFException ignored) {
        } catch (IOException e) {
            System.out.println("IOException has occurred.");

            Misc.promptErr(e);
        }  finally {
            // Have to remove the connection or else the program will be wasting resources 
            server.removeConnection(userName);
        }
    }
}
