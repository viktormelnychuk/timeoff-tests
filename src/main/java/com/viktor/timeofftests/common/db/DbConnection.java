package com.viktor.timeofftests.common.db;

import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;

@Log4j2
public class DbConnection {

    public static Connection getConnection(){
        String password = "1234";
        String user = "timeoff";
        String url = "jdbc:postgresql://localhost:5432/timeoff";
        try {
            log.info("Connecting to db as {}:{}", user, password);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            log.error("Error connecting to database as {}:{} on url={}",user, password, url, e);
            return null;
        }
    }
}
