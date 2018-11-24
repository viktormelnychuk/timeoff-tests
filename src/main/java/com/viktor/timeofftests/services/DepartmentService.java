package com.viktor.timeofftests.services;

import com.viktor.timeofftests.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class DepartmentService {
    private static DepartmentService departmentService;
    public static DepartmentService getInstance(){
        if(departmentService == null){
            return new DepartmentService();
        } else {
            return departmentService;
        }
    }
    private DepartmentService(){}

    public Department getOrCreateDepartmentWithName(String name, int companyId){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Departments\" WHERE name=? AND \"companyId\"= ? LIMIT 1";
        try {
            PreparedStatement getDepartment = connection.prepareStatement(sql);
            getDepartment.setString(1,name);
            getDepartment.setInt(2, companyId);
            log.info("Executing {}", getDepartment.toString());
            ResultSet resultSet = getDepartment.executeQuery();
            if (resultSet.next()){
                log.info("Found department with name {}", name);
                return deserializeDepartment(resultSet);
            } else {
                log.info("Department not found. Creating");
                return new Department.Builder()
                        .withName(name)
                        .inCompany(companyId)
                        .buildAndStore();
            }

        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        }
    }


    private Department deserializeDepartment(ResultSet set){
        try{
            return new Department.Builder()
                    .withName(set.getString("name"))
                    .withAllowance(set.getInt("allowance"))
                    .includePublicHolidays(set.getBoolean("include_public_holidays"))
                    .isAccuredAllowance(set.getBoolean("is_accrued_allowance"))
                    .inCompany(set.getInt("companyId"))
                    .withAdminUserId(set.getInt("bossId"))
                    .build();
        } catch (Exception e){
            log.error("Error deserializing department", e);
            return null;
        }
    }
}
