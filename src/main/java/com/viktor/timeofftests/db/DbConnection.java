package com.viktor.timeofftests.db;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;

@Log4j2
public class DbConnection {

    public static Connection getConnection(){
        try {
            String password = "1234";
            String user = "timeoff";
            String url = "jdbc:postgresql://localhost:5432/timeoff";
            log.info("Connecting to db as {}:{}", user, password);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
