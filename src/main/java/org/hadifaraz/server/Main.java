package org.hadifaraz.server;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server("jdbc:sqlite:db/chats.db", 60430);

            server.sendMsg("Server", "bals");
        } catch (Exception ignored) {}
    }
}