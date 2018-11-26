package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Session;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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

    public void insertSession (String sid){
        try {
            log.info("Inserting session with sid=[{}]", sid);
            Connection connection = DbConnection.getConnection();
            String sql = "INSERT INTO \"Sessions\" (sid, expires, data, \"createdAt\", \"updatedAt\") VALUES (?,?,?,?,?)";
            PreparedStatement insertSession = connection.prepareStatement(sql);
            insertSession.setString(1, sid);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Timestamp expires = new Timestamp(calendar.getTime().getTime());
            insertSession.setTimestamp(2, expires);
            insertSession.setString(3, "data");
            insertSession.setTimestamp(4, new Timestamp(new Date().getTime()));
            insertSession.setTimestamp(5, new Timestamp(new Date().getTime()));
            log.info("Executing {}", insertSession);
            insertSession.executeUpdate();
        } catch (Exception e){
            log.error("Error inserting session with sid=[{}]",sid,e);
        }
    }

    public Session insertNewSession (){
        String sid = generateSessionUid();
        insertSession(sid);
        return null;
    }

    private String generateSessionUid(){
        return RandomStringUtils.randomAlphanumeric(24);
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
