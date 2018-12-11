package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, companyId);
            return getDepartment(statement);
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Department getDepartmentWithId(int id) {
        Connection connection = DbConnection.getConnection();
        log.info("Getting department with id={}",id);
        try{
            String sql = "SELECT * FROM \"Departments\" WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            return getDepartment(statement);
        } catch (SQLException e){
            log.error("Error occurred", e);
            return null;
        }finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Department getDepartmentWithName (String name){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Departments\" WHERE name=? LIMIT 1;";
        log.info("Getting department with name [{}]", name);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            return getDepartment(statement);
        } catch (Exception e){
            log.error("Error ocurred", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    private Department getDepartment (PreparedStatement statement){
        log.info("Executing {}", statement);
        try {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return deserializeDepartment(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e){
            log.error("Error getting department", e);
            return null;
        }
    }

    public List<Department> insertDepartmentsForCompany(List<Department> departments){
        List<Department> result = new ArrayList<>();
        for (Department department : departments) {
            result.add(saveDepartment(department));
        }
        return result;
    }

    public List<Department> insertDepartments (Department... departments){
        return insertDepartmentsForCompany(Arrays.asList(departments));
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

            String selectSql = "SELECT * FROM \"Departments\" WHERE  \"Departments\".name = ? LIMIT 1";
            PreparedStatement selectDep = connection.prepareStatement(selectSql);
            selectDep.setString(1,department.getName());
            log.info("Executing {}", selectDep.toString());
            ResultSet set = selectDep.executeQuery();
            set.next();
            department.setId(set.getInt("id"));
            department.setCompanyId(set.getInt("companyId"));
            department.setBossId(set.getInt("bossId"));
            return department;

        } catch (Exception e){
            log.error("Error inserting department", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    public void assignBossUserId(Department department, int id){
        Connection connection = DbConnection.getConnection();
        String sql = "UPDATE \"Departments\" SET \"bossId\"=?, \"updatedAt\"=? WHERE \"id\"=?";
        try{
            PreparedStatement updateDepartmentWithBossId = connection.prepareStatement(sql);
            updateDepartmentWithBossId.setInt(1,id);
            updateDepartmentWithBossId.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            updateDepartmentWithBossId.setInt(3, department.getId());
            log.info("Updating department with id={} to have bossId={}", department.getId(), id);
            log.info("Executing {}",updateDepartmentWithBossId.toString());
            updateDepartmentWithBossId.executeUpdate();

        } catch (Exception e){
            log.error("Error updating department", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    private Department deserializeDepartment(ResultSet set){
        try{
            Department department = new Department();
            department.setName(set.getString("name"));
            department.setAllowance(set.getInt("allowance"));
            department.setAccuredAllowance(set.getBoolean("is_accrued_allowance"));
            department.setIncludePublicHolidays(set.getBoolean("include_public_holidays"));
            department.setId(set.getInt("id"));
            department.setCompanyId(set.getInt("companyId"));
            department.setBossId(set.getInt("bossId"));
            return department;
        } catch (Exception e){
            log.error("Error deserializing department", e);
            return null;
        }
    }

    public List<Department> deserializeDepartments(List<WebElement> table) {
        List<Department> result = new ArrayList<>();
        for (WebElement row:table) {
            Department department = new Department();
            String depLink = row.findElement(By.xpath(".//td[1]/a")).getAttribute("href");
            String depId = depLink.split("/departments/edit/")[1].split("/")[0];
            department.setId(Integer.parseInt(depId));

            String bossLink = row.findElement(By.xpath(".//td[2]/a")).getAttribute("href");
            try {
                String bossId = bossLink.split("/users/edit/")[1].split("/")[0];
                department.setBossId(Integer.parseInt(bossId));
            } catch (IndexOutOfBoundsException e){
                department.setBossId(0);
            }

            department.setName(row.findElement(By.xpath(".//td[1]")).getText());

            int allowance = Integer.parseInt(row.findElement(By.xpath(".//td[3]")).getText());
            department.setAllowance(allowance);

            String publicHolidays = row.findElement(By.xpath(".//td[5]")).getText();
            department.setIncludePublicHolidays(getBoolFromYesNo(publicHolidays));

            String accruedAllowance = row.findElement(By.xpath(".//td[6]")).getText();
            department.setAccuredAllowance(getBoolFromYesNo(accruedAllowance));
            department.setCompanyId(CompanyService.getInstance().getCompanyForDepartmentWithId(department.getId()).getId());
            result.add(department);
        }
        return result;
    }

    private boolean getBoolFromYesNo(String word){
        return Objects.equals(word, "Yes");
    }
}
