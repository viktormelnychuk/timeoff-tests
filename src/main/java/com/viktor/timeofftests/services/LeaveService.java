package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.Leave;
import com.viktor.timeofftests.models.LeaveDayPart;
import com.viktor.timeofftests.models.LeaveStatus;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Log4j2
public class LeaveService {

    private DepartmentService departmentService;
    public LeaveService(DepartmentService departmentService){

        this.departmentService = departmentService;
    }

    public Leave insertLeave(Leave leave){
        log.debug("Inserting leave {}", leave);
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"Leaves\" " +
                "(status, employee_comment, approver_comment, decided_at, date_start," +
                " day_part_start, date_end, day_part_end, \"userId\", \"approverId\", \"leaveTypeId\")" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, leave.getStatus().getValue());
            statement.setString(2, leave.getEmployeeComment());
            statement.setString(3, leave.getApproverComment());
            statement.setTimestamp(4, new Timestamp(leave.getDecidedAt().getTime()));
            statement.setTimestamp(5,new Timestamp(leave.getDateStart().getTime()));
            statement.setInt(6, leave.getDayPartStart().getValue());
            statement.setTimestamp(7, new Timestamp(leave.getDateEnd().getTime()));
            statement.setInt(8, leave.getDayPartEnd().getValue());
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

    public Leave insertLeave (int userId, int leaveTypeId, LeaveStatus status){
        Leave leave = new Leave();
        leave.setLeaveTypeId(leaveTypeId);
        leave.setUserId(userId);
        // find approver id
        Department departmentForUserId = departmentService.getDepartmentForUserId(userId);
        leave.setApproverId(departmentService.getBossId(departmentForUserId.getId()));
        leave.setDateStart(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        leave.setDateEnd(calendar.getTime());
        leave.setDayPartStart(LeaveDayPart.ALL);
        leave.setDayPartEnd(LeaveDayPart.ALL);
        return leave;
    }

    private Leave deserializeLeave(ResultSet set){
        Leave leave = new Leave();
        return leave;
    }
}
