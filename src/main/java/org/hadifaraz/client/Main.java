package org.hadifaraz.client;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // User should provide the hostname and port to connect to via CLI arguments
        if (args.length < 2) {
            System.out.println("Usage: Client <HOST> <PORT>");
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));

        JFrame gui = new JFrame();

        // GUI Setup
        // Defaults to being really small which got annoying very fast
        gui.setSize(1200, 600);
        gui.setLayout(new BorderLayout());
        gui.setVisible(true);

        // Add the client centered. Shouldn't matter too much as the Client GUI is always fullscreen
        gui.add("Center", client);

        client.setVisible(true);
    }
}
