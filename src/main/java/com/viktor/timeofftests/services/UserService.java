package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.DepartmentPool;
import com.viktor.timeofftests.pools.UserPool;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public class UserService {
    private static UserService userService;
    private CompanyService companyService = CompanyService.getInstance();
    public static UserService getInstance(){
        if(userService == null){
            return new UserService();
        } else {
            return userService;
        }
    }
    private UserService(){}

    public void makeDepartmentAdmin(User user){
        Connection connection = DbConnection.getConnection();
        String sql = "UPDATE \"Departments\" SET \"bossId\"=?, \"updatedAt\"=? WHERE id=(SELECT \"DepartmentId\" FROM \"Users\" WHERE id=?);";
        try {
            log.info("Preparing to set user with id {} a department admin", user.getId());
            PreparedStatement updateDepartment = connection.prepareStatement(sql);
            updateDepartment.setInt(1, user.getId());
            updateDepartment.setTimestamp(2, new Timestamp(new Date().getTime()));
            updateDepartment.setInt(3, user.getId());
            log.info("Executing {}", updateDepartment.toString());
            updateDepartment.executeUpdate();
        } catch (Exception e){
            log.error("Error setting department admin", e);
        }

    }

    public User createNewUser(User user){
        Connection connection = DbConnection.getConnection();
        String sql = "INSERT INTO \"Users\" (email, password, name, lastname, activated, admin, auto_approve, start_date," +
                " end_date, \"createdAt\", \"updatedAt\", \"companyId\", \"DepartmentId\")" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            log.info("Preparing to insert new user");
            PreparedStatement insertUser = connection.prepareStatement(sql);
            insertUser.setString(1, user.getEmail());
            insertUser.setString(2, user.getPassword());
            insertUser.setString(3,user.getName());
            insertUser.setString(4,user.getLastName());
            insertUser.setBoolean(5,user.isActivated());
            insertUser.setBoolean(6, user.isAdmin());
            insertUser.setBoolean(7, user.isAutoApprove());
            insertUser.setTimestamp(8,user.getStartDate() );
            insertUser.setTimestamp(9, user.getEndDate());
            insertUser.setTimestamp(10, new Timestamp(new Date().getTime()));
            insertUser.setTimestamp(11, new Timestamp(new Date().getTime()));
            insertUser.setInt(12, user.getCompanyID());
            insertUser.setInt(13, user.getDepartmentID());
            log.info("Executing {}",insertUser.toString());
            insertUser.executeUpdate();
            String getUserSql = "SELECT id FROM \"Users\" WHERE email=? LIMIT 1";
            PreparedStatement getUser = connection.prepareStatement(getUserSql);
            getUser.setString(1,user.getEmail());
            ResultSet resultSet = getUser.executeQuery();
            resultSet.next();
            user.setId(resultSet.getInt("id"));
            return user;
        } catch (Exception e){
            log.error("Error inserting user", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Company getCompanyForUser(User user){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Companies\" WHERE id=?";
        try {
            log.info("Getting company with id [{}]", user.getCompanyID());
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,user.getCompanyID());
            log.info("Executing {}", statement.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return companyService.deserializeCompany(resultSet);
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
    public List<User> getAllUsersInDepartment(Department department) {
        log.info("Getting all users in department id={}", department.getId());
        Connection connection = DbConnection.getConnection();
        List<User> users = new ArrayList<>();
        try{
            String sql = "SELECT * FROM \"Users\" WHERE \"DepartmentId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, department.getId());
            log.info("Executin {}", statement);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                users.add(deserializeUser(set));
            }
            return users;
        } catch (Exception e){
            log.error("Error getting all users in department id={}", department.getId(), e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
    public User getUserWithEmail(String email){
        log.info("Getting user with email={}", email);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"Users\" WHERE email=? LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            log.info("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeUser(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error getting user with email={}", email, e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public boolean userIsAdmin (String  userEmail){
       User user = getUserWithEmail(userEmail);
       return user.isAdmin();
    }

    public User createDefaultAdmin(){
        User user = new User.Builder()
                .isAdmin()
                .inCompany("Acme")
                .inDepartment("Sales")
                .isAutoApproved()
                .build();
        user = createNewUser(user);
        makeDepartmentAdmin(user);
        return user;
    }

    private User deserializeUser (ResultSet set){
        User user = new User();
        try{
            user.setId(set.getInt("id"));
            user.setEmail(set.getString("email"));
            user.setPassword(set.getString("password"));
            user.setName(set.getString("name"));
            user.setLastName(set.getString("lastname"));
            user.setActivated(set.getBoolean("activated"));
            user.setAdmin(set.getBoolean("admin"));
            user.setAutoApprove(set.getBoolean("auto_approve"));
            user.setStartDate(set.getTimestamp("start_date"));
            user.setEndDate(set.getTimestamp("end_date"));
            user.setCompanyID(set.getInt("companyId"));
            user.setDepartmentID(set.getInt("DepartmentId"));
            return user;
        } catch (Exception e){
            log.error("Error deserializing user", e);
            return null;
        }
    }

    public User createRandomUser() {
        User user = new User.Builder()
                .withEmail(UserPool.getEmail())
                .withName(UserPool.getName())
                .withLastName(UserPool.getLastName())
                .build();
        log.info("Creating user {}", user);
        return createNewUser(user);
    }

    public User createRandomUsersInDifferentDepartments() {
        User user = new User.Builder()
                .withEmail(UserPool.getEmail())
                .withName(UserPool.getName())
                .withLastName(UserPool.getLastName())
                .inDepartment(DepartmentPool.getDepartmentName())
                .build();
        log.info("Creating user {}", user);
        return createNewUser(user);
    }
}
