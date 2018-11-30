package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Schedule;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class ScheduleService {
    private static ScheduleService scheduleService;
    public static ScheduleService getInstance(){
        if(scheduleService == null){
            return new ScheduleService();
        } else {
            return scheduleService;
        }
    }
    private ScheduleService (){}

    public void insertDefaultSchedule( int companyId ){
        log.info("Preparing to insert default schedule for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"schedule\" (id, monday, tuesday, wednesday, thursday, friday, saturday, sunday, created_at, updated_at, company_id, user_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            Schedule schedule = new Schedule(companyId);
            PreparedStatement insert = connection.prepareStatement(sql);
            insert.setInt(1, schedule.getMonday() );
            insert.setInt(2, schedule.getTuesday() );
            insert.setInt(3, schedule.getWednesday() );
            insert.setInt(4, schedule.getThursday() );
            insert.setInt(5, schedule.getFriday() );
            insert.setInt(6, schedule.getSaturday() );
            insert.setInt(7, schedule.getSunday());
            insert.setTimestamp(8, schedule.getCreatedAt() );
            insert.setTimestamp(9, schedule.getUpdatedAt() );
            insert.setInt(10, schedule.getCompanyId());
            insert.setInt(11, schedule.getUserID());
            log.info("Executing {}", insert);
            insert.executeUpdate();
        } catch (Exception e){
            log.error("Error inserting schedule", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Schedule getScheduleForCompanyId(int companyId){
        log.info("Getting schedule for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM schedule WHERE \"company_id\"=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeSchedule(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error getting schedule", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }


    private Schedule deserializeSchedule(ResultSet set){
        try {
            Schedule schedule = new Schedule();
            schedule.setMonday(set.getInt("monday"));
            schedule.setTuesday(set.getInt("monday"));
            schedule.setWednesday(set.getInt("monday"));
            schedule.setThursday(set.getInt("thursday"));
            schedule.setFriday(set.getInt("friday"));
            schedule.setSaturday(set.getInt("saturday"));
            schedule.setSunday(set.getInt("sunday"));
            schedule.setCompanyId(set.getInt("company_id"));
            schedule.setUserID(set.getInt("user_id"));
            return schedule;
        } catch (Exception e){
            log.error("Error deserializing schedule ", e);
            return null;
        }
    }
}
