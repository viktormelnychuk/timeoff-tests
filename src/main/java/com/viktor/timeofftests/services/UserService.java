package com.viktor.timeofftests.services;

import com.viktor.timeofftests.db.DbConnection;
import com.viktor.timeofftests.models.User;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

@Log4j2
public class UserService {
    private static UserService userService;
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
