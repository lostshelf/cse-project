package org.hadifaraz.client;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("localhost", 60430);

        JFrame gui = new JFrame();

        gui.setSize(1200, 600);
        gui.setLayout(new BorderLayout());
        gui.setVisible(true);
        gui.add("Center", client);

        client.setVisible(true);
    }
}