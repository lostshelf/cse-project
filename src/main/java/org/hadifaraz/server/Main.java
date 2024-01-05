package org.hadifaraz.server;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            Server server = new Server("jdbc:sqlite:db/chats.db", 60430);
        } catch (Exception ignored) {}
    }
}