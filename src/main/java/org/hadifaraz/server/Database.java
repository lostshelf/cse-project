package org.hadifaraz.server;

import org.apache.commons.lang3.tuple.*;

import java.sql.*;
import java.util.*;

public class Database {
    // SQLite3 driver connection
    private Connection connection;

    public Database(String connection) {
        try {
            this.connection = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            System.out.println("Invalid database connection url.");

            Misc.promptErr(e);
        }
    }

    // Helper function to make it easier to run SQL commands
    private void execute(String command) throws SQLException {
        connection.createStatement().execute(command);
    }

    // Helper function to make querying the database easier
    private ResultSet executeQuery(String command) throws SQLException {
        return connection.createStatement().executeQuery(command);
    }

    // Helper function to make creating new database tables easier
    public void createTable(String name, ArrayList<String> columns) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("CREATE TABLE IF NOT EXISTS %s (", name));

        // Loop through each column and add it to the String builder
        for (int i = 0; i < columns.size(); i++) {
            builder.append(columns.get(i).trim());

            // Add the comma separator unless it is the last column
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

            Misc.promptErr(e);
        }
    }

    // Helper function to make adding messages to the database easier
    public void addMsg(String sender, String contents) {
        try {
            // Inserts the sender username and message content into the database
            execute(String.format("INSERT INTO messages (sender, content) VALUES (\"%s\", \"%s\")", sender, contents));
        } catch (Exception e) {
            System.out.println("Unable to upload message to database");

            Misc.promptErr(e);
        }
    }

    // Helper function to make it easier to retrieve all the messages from server
    public ArrayList<Pair<String, String>> getMsgs(int amount) {
        ArrayList<Pair<String, String>> msgs = new ArrayList<>();

        // Queries the database for the sender and content sorted by descending time        
        try (ResultSet res = executeQuery(String.format("SELECT sender, content FROM messages ORDER BY timedate DESC LIMIT %d", amount))) {
            // Loop through the result of the query and add each message to the messages ArrayList
            while (res.next())
                msgs.add(Pair.of(res.getString("sender").trim(), res.getString("content").trim()));
        } catch (Exception e) {
            System.out.println("Unable to retrieve message.");

            Misc.promptErr(e);
        }

        return msgs;
    }

    // By default, I only want it retrieving 50 messages
    public ArrayList<Pair<String, String>> getMsgs() {
        return getMsgs(50);
    }
}
