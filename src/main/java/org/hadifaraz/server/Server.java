package org.hadifaraz.server;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.commons.lang3.tuple.*;

public class Server {
    int port;
    ServerSocket socket;
    private final Database db;
    ArrayList<Triple<String, Socket, Pair<DataOutputStream, DataInputStream>>> connections;

    public Server(String dbUri, int port) throws IOException {
        db = new Database(dbUri);
        this.port = port;
        socket = new ServerSocket(port);
        connections = new ArrayList<>();

        listen();
    }

    private void listen() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {

                System.out.println("Waiting for connection...");
                Socket socket = this.socket.accept();

                System.out.println("Received connection.");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                String userName = dis.readUTF();
                System.out.printf("User %s has joined with address %s.%n", userName, socket.getInetAddress());

                connections.add(Triple.of(userName, socket, Pair.of(dos, dis)));

                System.out.printf("Starting %s's server thread.%n", userName);
                new ServerThread(userName, this, socket);

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

        for (int i = 0; i < connections.size(); i++)
            if (connections.get(i).getLeft().equals(userName)) {
                connections.remove(i);

                break;
            }
    }
}
