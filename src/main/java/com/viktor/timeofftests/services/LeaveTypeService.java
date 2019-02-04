package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.forms.LeaveTypeForm;
import com.viktor.timeofftests.models.LeaveType;
import lombok.extern.log4j.Log4j2;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@Log4j2
public class LeaveTypeService {
    public LeaveTypeService(){

    }

    public void insertLeaveTypes(int companyId, LeaveType... leaveTypes){
        log.debug("Inserting leave types: {}", Arrays.toString(leaveTypes));
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "INSERT INTO \"LeaveTypes\" (name, color, use_allowance, \"limit\", sort_order, \"createdAt\", \"updatedAt\", \"companyId\")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insert = connection.prepareStatement(sql);
            for (LeaveType leaveType: leaveTypes) {
                leaveType.setCompanyId(companyId);
                insert.setString(1, leaveType.getName());
                insert.setString(2, leaveType.getColorHex());
                insert.setBoolean(3, leaveType.isUseAllowance());
                insert.setInt(4, leaveType.getLimit());
                insert.setInt(5, leaveType.getSortOrder());
                insert.setTimestamp(6, new Timestamp(new Date().getTime()));
                insert.setTimestamp(7, new Timestamp(new Date().getTime()));
                insert.setInt(8, companyId);
                insert.addBatch();
            }
            log.debug("Executing {}", insert);
            insert.executeBatch();
        } catch (Exception e){
            log.error("Error inserting Leave types", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public int getCompanyIdForLeave(LeaveType leaveType){
        log.debug("Getting [companyId] for leave type with name={}", leaveType.getName());
        Connection connection = DbConnection.getConnection();
        try {
            String sql = "SELECT \"companyId\" FROM \"LeaveTypes\" WHERE name=? AND color=? AND \"limit\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, leaveType.getName());
            statement.setString(2, leaveType.getColorHex());
            statement.setInt(3, leaveType.getLimit());
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                return set.getInt("companyId");
            } else {
                return 0;
            }
        } catch (Exception e){
            log.error("Error getting company ID for leave type",e);
            return 0;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public List<LeaveType> getLeaveTypesForCompanyWithId(int companyId){
        log.debug("Getting leave types for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"LeaveTypes\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeLeaveTypes(set);
            } else {
                return new ArrayList<LeaveType>();
            }
        } catch (Exception e){
            log.error("Error getting leave types", e);
            return new ArrayList<LeaveType>();
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    public LeaveType findOneByForm(LeaveTypeForm form) {
        log.debug("Getting leave type with values {}", form);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"LeaveTypes\" WHERE color=? AND name=? AND use_allowance=? AND \"limit\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            //adjusting leave type color
            statement.setString(1, form.getColor());
            statement.setString(2, form.getName());
            statement.setBoolean(3, form.isUseAllowance());
            statement.setInt(4, form.getLimit());
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeLeaveType(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Erro occurred", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public LeaveType getLeaveTypeById(int toEditLeaveId) {
        log.debug("Getting leave type with id={}", toEditLeaveId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"LeaveTypes\" WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, toEditLeaveId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeLeaveType(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    private List<LeaveType> deserializeLeaveTypes(ResultSet set){
        List<LeaveType> leaveTypes = new ArrayList<>();
        try {
            do {
                leaveTypes.add(deserializeLeaveType(set));
            } while ((set.next()));
            return leaveTypes;
        } catch (Exception e){
            log.error("Error deserializing leave types from Database", e);
            return Collections.emptyList();
        }
    }

    private LeaveType deserializeLeaveType(ResultSet set){
        try{
            LeaveType leaveType = new LeaveType();
            leaveType.setId(set.getInt("id"));
            leaveType.setName(set.getString("name"));
            leaveType.setColorHex(set.getString("color"));
            leaveType.setUseAllowance(set.getBoolean("use_allowance"));
            leaveType.setLimit(set.getInt("limit"));
            leaveType.setSortOrder(set.getInt("sort_order"));
            leaveType.setCompanyId(set.getInt("companyId"));
            return leaveType;
        } catch (Exception e){
            log.error("Error deserializing leave types from Database", e);
            return null;
        }
    }


    public LeaveType getLeaveTypeByNameAndCompanyId(String leaveName, int companyId) {
        log.debug("Getting leave type with name=[{}] and companyId=[{}]", leaveName, companyId);
        Connection connection = DbConnection.getConnection();
        try {
            String sql = "SELECT * FROM \"LeaveTypes\" WHERE \"companyId\"=? AND name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            statement.setString(2, leaveName);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeLeaveType(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        }
    }
}
