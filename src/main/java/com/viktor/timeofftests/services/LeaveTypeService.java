package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.LeaveType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Log4j2
public class LeaveTypeService {
    public LeaveTypeService(){

    }

    public void insertLeaveTypes(int companyId, LeaveType... leaveTypes){
        log.info("Inserting leave types: {}", Arrays.toString(leaveTypes));
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
            log.info("Executing {}", insert);
            insert.executeBatch();
        } catch (Exception e){
            log.error("Error inserting Leave types", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public int getCompanyIdForLeave(LeaveType leaveType){
        log.info("Getting [companyId] for leave type with name={}", leaveType.getName());
        Connection connection = DbConnection.getConnection();
        try {
            String sql = "SELECT \"companyId\" FROM \"LeaveTypes\" WHERE name=? AND color=? AND \"limit\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, leaveType.getName());
            statement.setString(2, leaveType.getColorHex());
            statement.setInt(3, leaveType.getLimit());
            log.info("Executing {}", statement);
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
        log.info("Getting leave types for company with id={}", companyId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"LeaveTypes\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            log.info("Executing {}", statement);
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


    private List<LeaveType> deserializeLeaveTypes(ResultSet set){
        List<LeaveType> leaveTypes = new ArrayList<>();
        try {
            do {
                LeaveType leaveType = new LeaveType();
                leaveType.setId(set.getInt("id"));
                leaveType.setName(set.getString("name"));
                leaveType.setColorHex(set.getString("color"));
                leaveType.setUseAllowance(set.getBoolean("use_allowance"));
                leaveType.setLimit(set.getInt("limit"));
                leaveType.setSortOrder(set.getInt("sort_order"));
                leaveType.setCompanyId(set.getInt("companyId"));
                leaveTypes.add(leaveType);
            } while ((set.next()));
            return leaveTypes;
        } catch (Exception e){
            log.error("Error deserializing leave types from Database", e);
            return new ArrayList<LeaveType>();
        }
    }
}
