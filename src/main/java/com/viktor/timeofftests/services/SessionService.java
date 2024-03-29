package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Session;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Log4j2
public class SessionService {

    public Session getSessionWithSid(String sid){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Sessions\"  WHERE sid=?";
        try {
            log.debug("Getting session with sid=[{}]", sid);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sid);
            log.debug("Executing {}", preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return deserializeSession(resultSet);
        } catch (Exception e){
            log.error("Error getting session with sid=[{}]", sid, e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Session insertSession (String sid, String data){
        Connection connection = DbConnection.getConnection();
        try {
            log.debug("Inserting session with sid=[{}]", sid);
            String sql = "INSERT INTO \"Sessions\" (sid, expires, data, \"createdAt\", \"updatedAt\") VALUES (?,?,?,?,?)";
            PreparedStatement insertSession = connection.prepareStatement(sql);
            insertSession.setString(1, sid);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Timestamp expires = new Timestamp(calendar.getTime().getTime());
            insertSession.setTimestamp(2, expires);
            insertSession.setString(3, data);
            insertSession.setTimestamp(4, new Timestamp(new Date().getTime()));
            insertSession.setTimestamp(5, new Timestamp(new Date().getTime()));
            log.debug("Executing {}", insertSession);
            insertSession.executeUpdate();
            return getSessionWithSid(sid);
        } catch (Exception e){
            log.error("Error inserting session with sid=[{}]",sid,e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    public Session insertNewSessionForUserId (int userId){
        String sid = generateSessionUid();
        String cookiePart = "{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"}";
        String passportPart = String.format(",\"passport\":{\"user\":%s}}",userId);
        Session session = insertSession(sid, cookiePart+passportPart);
        return signSid(session);
    }

    private Session signSid(Session session){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(Constants.COOKIE_SIGNING_SECRET.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
            sha256_HMAC.init(secretKey);
            Base64.Encoder encoder = Base64.getEncoder();
            String signature = encoder.encodeToString(sha256_HMAC.doFinal(session.getSid().getBytes(StandardCharsets.UTF_8)));
            signature = signature.replaceAll("=+$","");
            String connectionSid = URLEncoder.encode("s:"+session.getSid()+"."+signature,StandardCharsets.UTF_8.toString());
            session.setSid(connectionSid);
        } catch (Exception ex){
            return null;
        }
        return session;
    }

    private String generateSessionUid(){
        return RandomStringUtils.randomAlphanumeric(32);
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
