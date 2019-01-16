package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.*;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Log4j2
public class LeaveService {

    private DepartmentService departmentService;
    private ScheduleService scheduleService;
    private BankHolidaysService bankHolidaysService;
    public LeaveService(DepartmentService departmentService, ScheduleService scheduleService, BankHolidaysService bankHolidaysService){

        this.departmentService = departmentService;
        this.scheduleService = scheduleService;
        this.bankHolidaysService = bankHolidaysService;
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

    public int amountOfUsedDays (int userId) throws Exception {
        int result = 0;
        List<LeaveLeaveType> allLeavesForUser = getAllLeavesForUserInThisYear(userId);
        for (LeaveLeaveType leaveLeaveType : allLeavesForUser) {
            Leave leave = leaveLeaveType.getLeave();
            LeaveType leaveType = leaveLeaveType.getLeaveType();
            LocalDate dateStart = leave.getDateStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateEnd = leave.getDateEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            for (LocalDate date = dateStart; date.isBefore(dateEnd); date=date.plusDays(1)){
                Schedule userSchedule = scheduleService.getScheduleForUserId(userId);
                if(userSchedule.isWorkingDay(date) && !bankHolidaysService.isHoliday(date, userId)){
                    result++;
                }
            }
//
//            double daysBetween = (double) ChronoUnit.DAYS.between(dateStart, dateEnd);
//            if(leave.getDayPartStart() != LeaveDayPart.ALL){
//                daysBetween = daysBetween - 0.5;
//            }
//            if(leave.getDayPartEnd() != LeaveDayPart.ALL){
//                daysBetween = daysBetween - 0.5;
//            }
//            result += daysBetween;
        }
        // this is total days, including holidays and other stuff. need to find a way to calculate
        // days that were used excluding bank holidays and non-working days (e.g. schedule settings)
        return result;
    }

    public List<LeaveLeaveType> getAllLeavesForUserInThisYear(int userId){
        log.debug("Getting all leaves for user with id=[{}]", userId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT\n" +
                    "       \"Leaves\".id as leave_id," +
                    "       \"Leaves\".status as leave_status," +
                    "       \"Leaves\".employee_comment as employee_comment," +
                    "       \"Leaves\".approver_comment as approver_comment," +
                    "       \"Leaves\".decided_at as decided_at," +
                    "       \"Leaves\".date_start as date_start," +
                    "       \"Leaves\".day_part_start as day_part_start," +
                    "       \"Leaves\".date_end as date_end," +
                    "       \"Leaves\".day_part_end as day_part_end," +
                    "       \"Leaves\".\"userId\" as user_id," +
                    "       \"Leaves\".\"approverId\" as approver_id," +
                    "       LT.id as leave_type_id," +
                    "       LT.name as leave_type_name," +
                    "       LT.color as leave_type_color," +
                    "       LT.use_allowance as leave_type_use_allowance," +
                    "       LT.\"companyId\" as leave_type_company_id," +
                    "       LT.sort_order as leave_type_sort_order," +
                    "       LT.\"limit\" as leave_type_limit " +
                    "FROM \"Leaves\" JOIN \"LeaveTypes\" LT on \"Leaves\".\"leaveTypeId\" = LT.id  " +
                    "WHERE \"userId\"=? AND date_start>=? AND date_end<=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            LocalDate firstDay = LocalDate.now().with(firstDayOfYear());
            LocalDate lastDay = LocalDate.now().with(lastDayOfYear());
            statement.setInt(1, userId);
            statement.setTimestamp(2, Timestamp.valueOf(firstDay.atStartOfDay()));
            statement.setTimestamp(3, Timestamp.valueOf(lastDay.atStartOfDay()));
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeLeaveLeaveType(set);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return Collections.emptyList();
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    private List<LeaveLeaveType> deserializeLeaveLeaveType(ResultSet set) {
        List<LeaveLeaveType> result = new ArrayList<>();
        try{
            do {
                LeaveLeaveType leaveLeaveType = new LeaveLeaveType();
                LeaveType leaveType = new LeaveType();
                Leave leave = new Leave();
                leave.setId(set.getInt("leave_id"));
                leave.setStatus(LeaveStatus.getByValue(set.getInt("leave_status")));
                leave.setEmployeeComment(set.getString("employee_comment"));
                leave.setApproverComment(set.getString("approver_comment"));
                leave.setDecidedAt(set.getDate("decided_at"));

                // forcing convertion to java.util.Date instead of java.sql.Date to use toInstant() later
                leave.setDateStart(new Date(set.getTimestamp("date_start").getTime()));
                leave.setDayPartStart(LeaveDayPart.getValuesOf(set.getInt("day_part_start")));
                leave.setDateEnd(new Date(set.getTimestamp("date_end").getTime()));
                leave.setDayPartEnd(LeaveDayPart.getValuesOf(set.getInt("day_part_end")));
                leave.setUserId(set.getInt("user_id"));
                leave.setApproverId(set.getInt("approver_id"));

                leaveType.setId(set.getInt("leave_type_id"));
                leaveType.setName(set.getString("leave_type_name"));
                leaveType.setColorHex(set.getString("leave_type_color"));
                leaveType.setUseAllowance(set.getBoolean("leave_type_use_allowance"));
                leaveType.setCompanyId(set.getInt("leave_type_company_id"));
                leaveType.setSortOrder(set.getInt("leave_type_sort_order"));
                leaveType.setLimit(set.getInt("leave_type_limit"));
                leaveLeaveType.setLeave(leave);
                leaveLeaveType.setLeaveType(leaveType);
                result.add(leaveLeaveType);
            } while (set.next());
            return result;
        } catch (Exception e){
            log.error("Error deserializing objects", e);
            return Collections.emptyList();
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
