package org.hadifaraz.client;

import org.hadifaraz.server.Misc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Client extends Panel implements Runnable {
    private final TextField tf = new TextField();
    private final TextArea ta = new TextArea();
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    public Client(String host, int port) {
        setLayout(new BorderLayout());

        add("South", tf);
        add("Center", ta);

        tf.addActionListener(e -> processMsg(e.getActionCommand()));

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server.");

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Data streams created.");

            new Thread(this).start();
            System.out.println("Thread started.");
        } catch (IOException ignored) {}
    }

    public void processMsg(String msg) {
        System.out.printf("Received message \"%s\" from client.%n", msg);

        try {
            dos.writeUTF(msg);
            System.out.println("Sent to server.");

            tf.setText("");
        } catch (IOException e) {
            Misc.promptErr(e);
        }
    }

    public void run() {
        try {
            while (true) {
                String msg = dis.readUTF();
                System.out.println("Received message from server.");

                ta.append(String.format("%s%n", msg));
                System.out.println("Displaying message.");
            }
        } catch (IOException ignored) {}
    }
}
