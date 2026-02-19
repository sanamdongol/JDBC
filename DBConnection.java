package testjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/student_db";

    // Database Username
    private static final String DB_USER = "root";

    // Database Password
    private static final String DB_PASSWORD = "";

    // Method to get connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database Connected Successfully!");

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
        return con;
    }
}
