package org.hadifaraz.client;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Main {
    private static String userName;
    public static void main(String[] args) {
        Client client = new Client("localhost", 60430);

        JFrame gui = new JFrame();
        gui.setSize(1400, 800);

        gui.setLayout(new BorderLayout());

        gui.setVisible(true);

        gui.add("Center", client);

        client.setVisible(true);
    }
}
