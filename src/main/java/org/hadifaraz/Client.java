package org.hadifaraz;

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

        tf.addActionListener((ActionListener) e -> processMsg(e.getActionCommand()));

        try {
            socket = new Socket(host, port);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(this).start();

        } catch (IOException e) {
            // TODO: Implement better error handling
        }
    }

    public void processMsg(String msg) {
        try {
            dos.writeUTF(msg);

            tf.setText("");
        } catch (IOException e) {
            // TODO: Implement error handling
        }
    }

    public void run() {
        try {
            while (true) {
                String msg = dis.readUTF();

                ta.append(String.format("%s%n", msg));
            }
        } catch (IOException e) {
            // TODO: Implement error handling or logging
        }
    }
}
