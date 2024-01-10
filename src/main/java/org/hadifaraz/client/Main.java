package org.hadifaraz.client;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: Client <HOST> <PORT>");
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));

        JFrame gui = new JFrame();

        gui.setSize(1200, 600);
        gui.setLayout(new BorderLayout());
        gui.setVisible(true);
        gui.add("Center", client);

        client.setVisible(true);
    }
}