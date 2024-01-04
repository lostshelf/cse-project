package org.hadifaraz.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Client extends Panel implements Runnable {
    private TextField tf = new TextField();
    private TextArea ta = new TextArea();
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    public Client(String host, int port) {

        setLayout(new BorderLayout());

        add("North", tf);
        add("Center", ta);

        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processMsg(e.getActionCommand());
            }
        });

        try {
            System.out.println("Connecting to server.");
            socket = new Socket(host, port);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(this).start();

        } catch (IOException ignored) {}
    }

    public void processMsg(String msg) {
        System.out.println("Got a message.");
        try {
            System.out.println("Sending to server.");
            dos.writeUTF(msg);

            tf.setText("");
        } catch (IOException ignored) {}
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Waiting for message from server.");
                String msg = dis.readUTF();

                System.out.println("Printing message");
                ta.append(String.format("%s%n", msg));
            }
        } catch (IOException ignored) {}
    }
}
