package org.hadifaraz.server;

import org.apache.commons.lang3.tuple.Pair;

import java.sql.*;
import java.util.*;

public class Database {
    private Connection connection;

    public Database(String connection) {
        try {
            this.connection = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            System.out.println("Invalid database connection url.");
        }
    }

    private void execute(String command) throws SQLException {
        connection.createStatement().execute(command);
    }

    private ResultSet executeQuery(String command) throws SQLException {
        return connection.createStatement().executeQuery(command);
    }

    public void createTable(String name, ArrayList<String> columns) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("CREATE TABLE IF NOT EXISTS %s (", name));

        for (int i = 0; i < columns.size(); i++) {
            builder.append(columns.get(i).trim());

            if (i != columns.size() - 1) {
                builder.append(',');
            }
        }

        builder.append(");");

        String command = builder.toString();

        try {
            execute(command);
        } catch (SQLException e) {
            System.out.println("Unable to create database table.");

            System.out.println(e.getMessage());
        }
    }

    public void addMsg(String sender, String contents) {
        try {
            execute(String.format("INSERT INTO messages (sender, contents) VALUES (%s, %s)", sender, contents));
        } catch (Exception e) {
            System.out.println("Unable to upload message to database");
        }
    }

    public ArrayList<Pair<String, String>> getMsgs(int amount) {
        ArrayList<Pair<String, String>> msgs = new ArrayList<>();

        try (ResultSet res = executeQuery(String.format("SELECT user_name, content FROM messages ORDER BY DESC time LIMIT %d", amount));) {
            while (res.next())
                msgs.add(Pair.of(res.getString("user_name"), res.getString("content")));
        } catch (Exception e) {
            System.out.println("Unable to retrieve message.");
        }

        return msgs;
    }

    public ArrayList<Pair<String, String>> getMsgs() {
        return getMsgs(50);
    }
}
