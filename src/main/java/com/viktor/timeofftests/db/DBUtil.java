package com.viktor.timeofftests.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    public static void cleanDB(){
        String url = "jdbc:postgresql://localhost:5432/timeoff";
        String user = "timeoff";
        String password = "1234";

        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();
            String sql = "truncate table \"BankHolidays\", \"Leaves\",  \"Companies\", \"DepartmentSupervisor\", \"Departments\", \"EmailAudit\", \"LeaveTypes\", \"Sessions\", \"UserFeeds\", \"Users\", \"schedule\", \"user_allowance_adjustment\";";
            statement.execute(sql);
        } catch (SQLException exception){
            exception.printStackTrace();
        }

    }
}
