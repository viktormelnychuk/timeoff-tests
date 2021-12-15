package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Department;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Log4j2
public class DepartmentService {
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
        log.debug("Getting department with name=[{}] and companyId=[{}]",name,companyId);
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
        log.debug("Getting department with id={}",id);
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
        log.debug("Getting department with name [{}]", name);
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

    public List<Department> getAllDepartmentsForCompany(int companyID) {
        log.debug("Getting all departments for company with id={}", companyID);
        Connection connection = DbConnection.getConnection();
        try{
            List<Department> result = new ArrayList<>();
            String sql = "SELECT * FROM \"Departments\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyID);
            log.debug("Executing {}", statement);
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
        log.debug("Executing {}", statement);
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

    public Integer getAmountOfUserInDepartment(Department department){
        log.debug("Getting amount of users in department {}", department);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"Users\" WHERE \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, department.getId());
            log.debug("Executing {}", statement);
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
        log.debug("Getting all users excluding admin for department id={}", departmetnId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT \"Users\".id FROM \"Users\" WHERE \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmetnId);
            log.debug("Executing {}", statement);
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
        log.debug("Getting department supervisors");
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"DepartmentSupervisor\" WHERE department_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.debug("Executing {}", statement);
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


    public void assignSecondarySupervisor(int departmentID) throws Exception {
        log.debug("Starting to assign secondary supervisors");
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "INSERT INTO \"DepartmentSupervisor\" (department_id, user_id, created_at)" +
                    " VALUES(?," +
                    "        (SELECT \"Users\".id FROM \"Users\" WHERE" +
                    "              \"Users\".id !=(SELECT \"Departments\".\"bossId\" FROM \"Departments\" WHERE \"Departments\".id = ?) AND" +
                    "              \"Users\".id NOT IN (SELECT \"DepartmentSupervisor\".user_id FROM \"DepartmentSupervisor\")" +
                    "AND \"Users\".\"DepartmentId\" = ?"+
                    " LIMIT 1)," +
                    "        ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentID);
            statement.setInt(2, departmentID);
            statement.setInt(3, departmentID);
            statement.setTimestamp(4, new Timestamp(new Date().getTime()));
            log.debug("Executing {}", statement);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected == 0){
                throw new Exception("Error assigning supervisors. Make sure there are non-manager users in company");
            }
        } catch (SQLException e){
            log.error("Error occurred", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    public void assignSecondarySupervisors(int departmentID ,int count) throws Exception {
        for(int i = 0; i < count; i++){
            assignSecondarySupervisor(departmentID);
        }
    }
    public Department createDepartment(Department department){
        log.debug("Inserting new department {}", department);
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"Departments\" (name, allowance, include_public_holidays, is_accrued_allowance, \"createdAt\", \"updatedAt\", \"companyId\", \"bossId\") VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try{
            PreparedStatement insertDepartment = connection.prepareStatement(sql);
            insertDepartment.setString(1, department.getName());
            insertDepartment.setDouble(2, department.getAllowance());
            insertDepartment.setBoolean(3,department.isIncludePublicHolidays());
            insertDepartment.setBoolean(4,department.isAccuredAllowance());
            insertDepartment.setTimestamp(5,new Timestamp(new java.util.Date().getTime()));
            insertDepartment.setTimestamp(6,new Timestamp(new java.util.Date().getTime()));
            insertDepartment.setInt(7, department.getCompanyId());
            insertDepartment.setInt(8, department.getBossId());
            log.debug("Executing {}",insertDepartment.toString());
            insertDepartment.executeUpdate();

            log.debug("Getting id of department with name {}",department.getName());

            String selectSql = "SELECT * FROM \"Departments\" WHERE  \"Departments\".name = ? LIMIT 1";
            PreparedStatement selectDep = connection.prepareStatement(selectSql);
            selectDep.setString(1,department.getName());
            log.debug("Executing {}", selectDep.toString());
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
            log.debug("Updating department with id={} to have bossId={}", department.getId(), id);
            log.debug("Executing {}",updateDepartmentWithBossId.toString());
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
            if(allUsers.isEmpty()) continue;
            assignBossUserId(department, allUsers.get(allUsers.size()/2));
        }
    }

    private List<Integer> getAllUsersInDepartment(int departmentId) {
        Connection connection = DbConnection.getConnection();
        log.debug("Getting all users in department with id={}", departmentId);
        try {
            String sql = "SELECT \"Users\".id FROM \"Users\" WHERE  \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.debug("Executing {}",statement);
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

    public Map<String, String> getManagersForDepartments (List<Department> departments){
        Map<String, String> result = new HashMap<>();
        for (Department department : departments) {
            result.put(department.getName(), getBossName(department.getId()));
        }
        return result;
    }
    public String getBossName(int departmentId){
        log.debug("Getting department manager for department with id={}", departmentId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT name, lastname from \"Users\" WHERE id= " +
                    "(SELECT \"bossId\" FROM \"Departments\" WHERE id=?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return set.getString("name")+" " + set.getString("lastname");
            } else {
                return StringUtils.EMPTY;
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return StringUtils.EMPTY;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Department getDepartmentForUserId(int userId){
        log.debug("Getting department for user with id=[{}]", userId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"Departments\" WHERE " +
                    "\"Departments\".id=(SELECT \"DepartmentId\" FROM \"Users\" WHERE \"Users\".id=?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeDepartment(set);
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

    public int getBossId(int departmentId){
        log.debug("Getting boss id for department with id=[{}]", departmentId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT \"bossId\" FROM \"Departments\" WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,departmentId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return set.getInt("bossId");
            } else {
                return 0;
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return 0;
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
