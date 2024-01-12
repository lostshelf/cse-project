package org.hadifaraz.server;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.commons.lang3.tuple.*;

public class Server {
    // Port this server should be running on
    int port;

    // Socket to read data from
    ServerSocket socket;

    // Database to store messages in
    private final Database db;

    // Client information
    // First string is the client's username
    ArrayList<Triple<String, Socket, Pair<DataOutputStream, DataInputStream>>> connections;

    public Server(String dbUri, int port) throws IOException {
        db = new Database(dbUri);
        this.port = port;
        socket = new ServerSocket(port);
        connections = new ArrayList<>();

        listen();
    }

    // Will run on the main Server thread
    private void listen() {
        // Had to add this so IntelliJ would stop whining about the infinite loop
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                System.out.println("Waiting for connection...");
                Socket socket = this.socket.accept();

                System.out.println("Received connection.");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // First message received from the client should be the Client's username
                String userName = dis.readUTF();
                System.out.printf("User %s has joined with address %s.%n", userName, socket.getInetAddress());

                connections.add(Triple.of(userName, socket, Pair.of(dos, dis)));

                System.out.printf("Starting %s's server thread.%n", userName);

                // Multithreading so that messages can be send and recieved by many clients at the same time
                new ServerThread(userName, this, socket);

                // Send the first 50 messages to the client
                ArrayList<Pair<String, String>> msgs = db.getMsgs();

                for (Pair msg : msgs) {
                    dos.writeUTF(String.format("%s: %s%n", msg.getLeft(), msg.getRight()));
                }

            } catch (IOException e) {
                Misc.promptErr(e);
            }
        }
    }

    public void sendMsg(String userName, String msg) {
        try {
            System.out.printf("%s says \"%s\".%n", userName, msg);

            // Send a message to every client
            for (var connection : connections) {
                System.out.printf("Sent message to %s.%n", connection.getLeft());
                connection.getRight().getLeft().writeUTF(String.format("%s: %s", userName, msg));
            }
        } catch (IOException e) {
            Misc.promptErr(e);
        }

        db.addMsg(userName, msg);
        System.out.println("Added message to database.");
    }

    public void removeConnection(String userName) {
        System.out.printf("Removing %s from the server.%n", userName);

        // Using an index is better than letting Java figure out which one to remove in a for each loop
        for (int i = 0; i < connections.size(); i++)
            if (connections.get(i).getLeft().equals(userName)) {
                connections.remove(i);

                break;
            }
    }
}
