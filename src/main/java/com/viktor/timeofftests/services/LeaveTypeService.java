package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.LeaveType;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

@Log4j2
public class LeaveTypeService {
    private static LeaveTypeService leaveTypeService;
    public static LeaveTypeService getInstance(){
        if(leaveTypeService == null){
            return new LeaveTypeService();
        } else {
            return leaveTypeService;
        }
    }
    private LeaveTypeService(){}

    void insertLeaveTypes(LeaveType[] leaveTypes, String companyName){
        log.info("Inserting leave types: {}", Arrays.toString(leaveTypes));
        Company company = CompanyService.getInstance().getCompanyWithName(companyName);
        try{
            Connection connection = DbConnection.getConnection();
            String sql = "INSERT INTO \"LeaveTypes\" (name, color, use_allowance, \"limit\", sort_order, \"createdAt\", \"updatedAt\", \"companyId\")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insert = connection.prepareStatement(sql);
            for (LeaveType leaveType: leaveTypes) {
                leaveType.setCompanyId(company.getId());
                insert.setString(1, leaveType.getName());
                insert.setString(2, leaveType.getColorHex());
                insert.setBoolean(3, leaveType.isUseAllowance());
                insert.setInt(4, leaveType.getLimit());
                insert.setInt(5, leaveType.getSortOrder());
                insert.setTimestamp(6, leaveType.getCreatedAt());
                insert.setTimestamp(7, leaveType.getUpdatedAt());
                insert.setInt(8, leaveType.getCompanyId());
                insert.addBatch();
            }
            log.info("Executing {}", insert);
            insert.executeBatch();
        } catch (Exception e){
            log.error("Error inserting Leave types", e);
        }
    }
}
