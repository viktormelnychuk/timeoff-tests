package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.*;
import java.util.*;

@Log4j2
public class DepartmentService {
    private CompanyService companyService;
    public DepartmentService(CompanyService companyService){
        this.companyService = companyService;
    }

    public Department getOrCreateDepartmentWithName(String name, int companyId){
        Department department = getDepartmentWithNameAndCompanyId(name, companyId);
        if (department == null) {
            Department newDepartment = new Department.Builder()
                    .withName(name)
                    .inCompany(companyId)
                    .build();
            return createDepartment(newDepartment);
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
    public Department getOneExistingDepartment(int companyID) {
        log.info("Getting 1 department in company with id={}", companyID);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"Departments\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, companyID);
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            set.last();
            if(set.getRow() > 1){
                throw new Exception(String.format("Company with id=%d contains more than 1 department." +
                        " Please set departmentId explicitly", companyID));
            }
            set.first();
            return deserializeDepartment(set);
        } catch (Exception e){
            log.error("Error getting department for company with id={}", companyID, e);
            return null;
        }
    }

    public List<Department> getAllDepartmentsForCompany(int companyID) {
        log.info("Getting all departments for company with id={}", companyID);
        Connection connection = DbConnection.getConnection();
        try{
            List<Department> result = new ArrayList<>();
            String sql = "SELECT * FROM \"Departments\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyID);
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(deserializeDepartment(set));
            }
            return result;
        } catch (Exception e){
            log.error("Error getting departments", e);
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
            result.add(createDepartment(department));
        }
        return result;
    }

    public List<Department> insertDepartments (Department... departments){
        return insertDepartmentsForCompany(Arrays.asList(departments));
    }

    public Integer getAmountOfUserInDepartment(Department department){
        log.info("Getting amount of users in department {}", department);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"Users\" WHERE \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, department.getId());
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            set.last();
            return set.getRow();
        } catch (Exception e){
            log.error("Error getting users for department {}", department);
            return null;
        }
    }

    public Map<String, Integer> getAmountOfUsersInDepartments(List<Department> departments){
        Map<String, Integer> result = new HashMap<>();
        for (Department department : departments) {
            result.put(department.getName(), getAmountOfUserInDepartment(department));
        }
        return result;
    }

    public List<Integer> getAllUsersExcludingAdmin(int departmetnId){
        log.info("Getting all users excluding admin for department id={}", departmetnId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT \"Users\".id FROM \"Users\" WHERE \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmetnId);
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            List<Integer> users = new ArrayList<>();
            int departmentAdminId = getDepartmentWithId(departmetnId).getBossId();
            while (set.next()){
                if(set.getInt("id")!=departmentAdminId){
                    users.add(set.getInt("id"));
                }

            }
            return users;
        } catch (Exception e){
            log.error("Error occurred", e);
            return Collections.emptyList();
        }
    }

    public List<Integer> getDepartmentSupervisors(int departmentId){
        log.info("Getting department supervisors");
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"DepartmentSupervisor\" WHERE department_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            List<Integer> users = new ArrayList<>();
            while (set.next()){
                users.add(set.getInt("user_id"));
            }
            return users;
        } catch (Exception e){
            log.error("Error occurred", e);
            return Collections.emptyList();
        }
    }


    public Department createDepartment(Department department){
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

    public void setBossesForAllDepartments(int companyID){
        List<Department> departments = getAllDepartmentsForCompany(companyID);
        for (Department department : departments) {
            List<Integer> allUsers = getAllUsersInDepartment(department.getId());
            assignBossUserId(department, allUsers.get(allUsers.size()/2));
        }
    }

    private List<Integer> getAllUsersInDepartment(int departmentId) {
        Connection connection = DbConnection.getConnection();
        log.info("Getting all users in department with id={}", departmentId);
        try {
            String sql = "SELECT \"Users\".id FROM \"Users\" WHERE  \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.info("Executin {}",statement);
            ResultSet set = statement.executeQuery();
            List<Integer> result = new ArrayList<>();
            while (set.next()){
                result.add(set.getInt("id"));
            }
            return result;
        } catch (Exception e){
            log.error("Exception occurred", e);
            return Collections.emptyList();
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
}
