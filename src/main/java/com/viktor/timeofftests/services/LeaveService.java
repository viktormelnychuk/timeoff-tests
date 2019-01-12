package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Leave;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@Log4j2
public class LeaveService {

    public Leave insertLeave(Leave leave){
        log.debug("Inserting leave {}", leave);
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"Leaves\" " +
                "(status, employee_comment, approver_comment, decided_at, date_start," +
                " day_part_start, date_end, day_part_end, \"userId\", \"approverId\", \"leaveTypeId\")" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, leave.getStatus());
            statement.setString(2, leave.getEmployeeComment());
            statement.setString(3, leave.getApproverComment());
            statement.setTimestamp(4, new Timestamp(leave.getDecidedAt().getTime()));
            statement.setTimestamp(5,new Timestamp(leave.getDateStart().getTime()));
            statement.setInt(6, leave.getDayPartStart());
            statement.setTimestamp(7, new Timestamp(leave.getDateEnd().getTime()));
            statement.setInt(8, leave.getDayPartEnd());
            statement.setInt(9,leave.getUserId());
            statement.setInt(10, leave.getApproverId());
            statement.setInt(11, leave.getLeaveTypeId());
            log.debug("Executing {}", statement);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected != 1){
                throw new Exception("Leave was not inserted");
            }
            log.debug("Getting id of inserted leave type");
            String getOneSql = "SELECT id FROM \"Leaves\" ORDER BY id DESC LIMIT 1";
            PreparedStatement getOne = connection.prepareStatement(getOneSql);
            log.info("Executing {}", getOne);
            ResultSet set = getOne.executeQuery();
            if(set.next()){
                leave.setId(set.getInt("id"));
            } else {
                throw new Exception("Error occurred");
            }
            return leave;
        } catch (Exception e){
            log.error("Error occured", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    private Leave deserializeLeave(ResultSet set){
        Leave leave = new Leave();
        return leave;
    }
}
