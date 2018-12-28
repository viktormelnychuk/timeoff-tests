package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Schedule;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Log4j2
public class ScheduleService {
    public  ScheduleService (){}

    public void insertDefaultSchedule( int companyId ){
        log.debug("Preparing to insert default schedule for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"schedule\" (monday, tuesday, wednesday, thursday, friday, saturday, sunday, created_at, updated_at, company_id, user_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
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
            insert.setTimestamp(8, new Timestamp(new Date().getTime()));
            insert.setTimestamp(9, new Timestamp(new Date().getTime()));
            insert.setObject(10, schedule.getCompanyId());
            insert.setObject(11, schedule.getUserID());
            log.debug("Executing {}", insert);
            insert.executeUpdate();
        } catch (Exception e){
            log.error("Error inserting schedule", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Schedule getScheduleForCompanyId(int companyId){
        log.debug("Getting schedule for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM schedule WHERE \"company_id\"=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            log.debug("Executing {}", statement);
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
            schedule.setId(set.getInt("id"));
            schedule.setMonday(set.getInt("monday"));
            schedule.setTuesday(set.getInt("tuesday"));
            schedule.setWednesday(set.getInt("wednesday"));
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

    public Schedule deserializeSchedule (List<WebElement> list){
        Schedule schedule = new Schedule();
        schedule.setMonday(isCheckedScheduleDay(list.get(0).getAttribute("checked")));
        schedule.setTuesday(isCheckedScheduleDay(list.get(1).getAttribute("checked")));
        schedule.setWednesday(isCheckedScheduleDay(list.get(2).getAttribute("checked")));
        schedule.setThursday(isCheckedScheduleDay(list.get(3).getAttribute("checked")));
        schedule.setFriday(isCheckedScheduleDay(list.get(4).getAttribute("checked")));
        schedule.setSaturday(isCheckedScheduleDay(list.get(5).getAttribute("checked")));
        schedule.setSunday(isCheckedScheduleDay(list.get(6).getAttribute("checked")));
        return schedule;
    }

    private int isCheckedScheduleDay (String checkedValue){
        if (Objects.equals(checkedValue, "true")){
            return 1;
        } else {
            return 2;
        }
    }
}
