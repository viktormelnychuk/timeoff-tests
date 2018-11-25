package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.User;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 2);
            Timestamp time = new Timestamp(calendar.getTime().getTime());
            PreparedStatement insertUser = connection.prepareStatement(sql);
            insertUser.setString(1, user.getEmail());
            insertUser.setString(2, user.getPassword());
            insertUser.setString(3,user.getName());
            insertUser.setString(4,user.getLastName());
            insertUser.setBoolean(5,user.isActivated());
            insertUser.setBoolean(6, user.isAdmin());
            insertUser.setBoolean(7, user.isAutoApprove());
            insertUser.setTimestamp(8,user.getStartDate() );
            insertUser.setTimestamp(9, time);
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
            return companyService.deserializeComapny(resultSet);
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        }
    }

    public boolean userIsAdmin (String  userEmail){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT \"admin\" FROM \"Users\" WHERE email=? LIMIT 1";
        try {
            log.info("Validating user with email {} is admin", userEmail);
            PreparedStatement selectUser = connection.prepareStatement(sql);
            selectUser.setString(1, userEmail);
            log.info("Executing {}", selectUser.toString());
            ResultSet resultSet = selectUser.executeQuery();
            resultSet.next();
            return resultSet.getBoolean("admin");
        } catch (Exception e){
            log.error("Error fetching user", e);
            return false;
        }
    }

}
