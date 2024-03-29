package org.hadifaraz.client;

import org.hadifaraz.server.Misc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Client extends JComponent implements Runnable {
    // Where user inputs their messages
    private final JTextField input = new JTextField();

    // Where the Server's messages are displayed
    private final JTextArea dislpay = new JTextArea();

    // Server connection information
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    
    public Client(String host, int port) {
        // GUI Setup
        setLayout(new BorderLayout());

        display.setEditable(false);
        display.setText("Please enter your username.");

        add("South", input);
        add("Center", display);

        input.addActionListener(e -> processMsg(e.getActionCommand()));

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server.");

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Data streams created.");

            // Multithreading so it can send and recieve messages at the same time
            new Thread(this).start();
            System.out.println("Thread started.");
        } catch (IOException ignored) {}
    }

    public void processMsg(String msg) {
        System.out.printf("Received message \"%s\" from client.%n", msg);

        try {
            dos.writeUTF(msg);
            System.out.println("Sent to server.");

            // Set the input field back to being empty
            input.setText("");
        } catch (IOException e) {
            Misc.promptErr(e);
        }
    }

    public void run() {
        try {
            // Keep reading messages until the Client exits
            while (true) {
                String msg = dis.readUTF();
                System.out.println("Received message from server.");

                // Dislpay message on the screen
                display.append(String.format("%s%n", msg));
                System.out.println("Displaying message.");
            }
        } catch (IOException ignored) {}
    }
}
