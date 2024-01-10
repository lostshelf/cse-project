package org.hadifaraz.server;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Server <PORT>");

            return;
        }

        try {
            Server server = new Server("jdbc:sqlite:db/chats.db", Integer.parseInt(args[0]));
        } catch (Exception ignored) {}
    }
}