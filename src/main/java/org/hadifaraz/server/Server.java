package org.hadifaraz.server;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.commons.lang3.tuple.*;

public class Server {
    int port;
    ServerSocket socket;
    private Database db;
    ArrayList<Triple<String, Socket, Pair<DataOutputStream, DataInputStream>>> connections;

    public Server(String dbUri, int port) throws IOException {
        db = new Database(dbUri);
        this.port = port;
        socket = new ServerSocket(port);
        connections = new ArrayList<>();

        listen();
    }

    private void listen() {
        while (true) {
            try {

                System.out.println("Waiting for connection...");
                Socket socket = this.socket.accept();

                System.out.println("Received connection.");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());


                System.out.println("Getting username from client.");
                String userName = dis.readUTF();
                System.out.printf("User %s has joined with address %s.%n", userName, socket.getInetAddress());

                connections.add(Triple.of(userName, socket, Pair.of(dos, dis)));

                System.out.printf("Starting %s's server thread.%n", userName);
                new ServerThread(userName, this, socket);
            } catch (IOException ignored) {}
        }
    }

    public void sendMsg(String userName, String msg) {

        System.out.printf("%s says \"%s\".%n", userName, msg);

        try {
            for (var connection : connections)
                connection.getRight().getLeft().writeUTF(msg);
        } catch (IOException ignored) {}

        System.out.println("Adding message to database");
        db.addMsg(userName, msg);
    }

    public void removeConnection(String userName) {

        System.out.printf("Removing %s from the server.%n", userName);

        for (int i = 0; i < connections.size(); i++)
            if (connections.get(i).getLeft().equals(userName)) {
                connections.remove(i);

                break;
            }
    }
}
