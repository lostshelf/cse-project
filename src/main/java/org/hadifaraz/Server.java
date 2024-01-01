package org.hadifaraz;

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
                Socket socket = this.socket.accept();

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                String userName = dis.readUTF();

                connections.add(Triple.of(userName, socket, Pair.of(dos, dis)));

            } catch (IOException e) {
                continue;
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            for (var connection : connections)
                connection.getRight().getLeft().writeUTF(msg);
        } catch (IOException e) {
            // TODO: Implement better error handling
        }
    }

    public void removeConnection(String userName) {

        for (int i = 0; i < connections.size(); i++)
            if (connections.get(i).getLeft().equals(userName)) {
                connections.remove(i);

                break;
            }

    }
}
