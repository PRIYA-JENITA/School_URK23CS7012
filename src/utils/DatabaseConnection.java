package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/school_management";
    private static final String USER = "root"; // Replace with your actual username
    private static final String PASSWORD = "root"; // Replace with your actual password
    
    // Get a new connection each time this method is called
    public static Connection getConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create and return a new connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Set auto-commit to true
            conn.setAutoCommit(true);
            
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found!", e);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database!", e);
        }
    }
    
    // Utility method to safely close a connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }
}
