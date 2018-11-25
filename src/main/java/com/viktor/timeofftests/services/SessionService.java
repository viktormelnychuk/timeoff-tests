package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Session;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class SessionService {
    private static SessionService sessionService;
    public static SessionService getInstance(){
        if(sessionService == null){
            return new SessionService();
        } else {
            return sessionService;
        }
    }
    private SessionService(){}

    public Session getSessionWithSid(String sid){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Sessions\"  WHERE sid=?";
        try {
            log.info("Getting session with sid=[{}]", sid);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sid);
            log.info("Executing {}", preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return deserializeSession(resultSet);
        } catch (Exception e){
            log.error("Error getting session with sid=[{}]", sid, e);
            return null;
        }
    }

    public Session deserializeSession (ResultSet resultSet){
        try {
            return new Session(
                    resultSet.getString("sid"),
                    resultSet.getString("data"),
                    resultSet.getDate("expires"),
                    resultSet.getDate("createdAt"),
                    resultSet.getDate("updatedAt")
            );
        } catch (Exception e){
            log.error("Error deserializing session", e);
            return null;
        }
    }
}
