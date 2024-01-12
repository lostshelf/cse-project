package org.hadifaraz.server;

public class Main {
    public static void main(String[] args) {
        // User must provide the port to run on via CLI args
        if (args.length == 0) {
            System.out.println("Usage: Server <PORT>");

            return;
        }

        try {
            // Create a new server
            // Will need to change the first argument depending on where the database is and what the SQLite driver is
            Server server = new Server("jdbc:sqlite:db/chats.db", Integer.parseInt(args[0]));
        } catch (Exception ignored) {}
    }
}
