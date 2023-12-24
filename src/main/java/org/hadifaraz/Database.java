package org.hadifaraz;

import java.sql.*;
import java.util.*;

public class Database {
    private Connection connection;

    public Database(String connection) {
        try {
            this.connection = DriverManager.getConnection(connection);
        } catch (SQLException e) {
            System.out.println("Invalid database connection url.");

            // TODO: Implement better exception handling
        }
    }

    public Optional<DatabaseMetaData> getMetadata() {
        try {
            return Optional.of(connection.getMetaData());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private void execute(String command) throws SQLException {
        connection.createStatement().execute(command);

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
}
