package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/museum_db", "root", "root@123");
            return con;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}