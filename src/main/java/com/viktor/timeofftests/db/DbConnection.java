package com.viktor.timeofftests.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    static String url = "jdbc:postgresql://localhost:5432/timeoff";
    static String user = "timeoff";
    static String password = "1234";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
