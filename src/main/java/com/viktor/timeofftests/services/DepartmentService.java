package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

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
        Department department = getDepartmentWithNameAndCompanyId(name, companyId);
        if (department == null) {
            Department newDepartment = new Department.Builder()
                    .withName(name)
                    .inCompany(companyId)
                    .build();
            return saveDepartment(newDepartment);
        } else {
            return department;
        }
    }

    public Department getDepartmentWithNameAndCompanyId(String name, int companyId){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Departments\" WHERE name=? AND \"companyId\"= ? LIMIT 1";
        log.info("Getting department with name=[{}] and companyId=[{}]",name,companyId);
        try {
            PreparedStatement getDepartment = connection.prepareStatement(sql);
            getDepartment.setString(1, name);
            getDepartment.setInt(2, companyId);
            log.info("Executing {}", getDepartment.toString());
            ResultSet resultSet = getDepartment.executeQuery();
            resultSet.next();
            return deserializeDepartment(resultSet);
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        }
    }
    public Department getDepartmentWithName (String name){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Departments\" WHERE name=? LIMIT 1;";
        log.info("Getting department with name [{}]", name);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            log.info("Executing {}", statement);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return deserializeDepartment(resultSet);
        } catch (Exception e){
            log.error("Error ocurred", e);
            return null;
        }
    }

    public Department saveDepartment(Department department){
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"Departments\" (name, allowance, include_public_holidays, is_accrued_allowance, \"createdAt\", \"updatedAt\", \"companyId\", \"bossId\") VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try{
            PreparedStatement insertDepartment = connection.prepareStatement(sql);
            insertDepartment.setString(1, department.getName());
            insertDepartment.setInt(2, department.getAllowance());
            insertDepartment.setBoolean(3,department.isIncludePublicHolidays());
            insertDepartment.setBoolean(4,department.isAccuredAllowance());
            insertDepartment.setTimestamp(5,new Timestamp(new java.util.Date().getTime()));
            insertDepartment.setTimestamp(6,new Timestamp(new java.util.Date().getTime()));
            insertDepartment.setInt(7, department.getCompanyId());
            insertDepartment.setInt(8, department.getBossId());
            log.info("Executing {}",insertDepartment.toString());
            insertDepartment.executeUpdate();

            log.info("Getting id of department with name {}",department.getName());

            String selectSql = "SELECT id FROM \"Departments\" WHERE  \"Departments\".name = ? LIMIT 1";
            PreparedStatement selectDep = connection.prepareStatement(selectSql);
            selectDep.setString(1,department.getName());
            log.info("Executing {}", selectDep.toString());
            ResultSet set = selectDep.executeQuery();
            set.next();
            department.setId(set.getInt("id"));
            return department;

        } catch (Exception e){
            log.error("Error inserting department", e);
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
