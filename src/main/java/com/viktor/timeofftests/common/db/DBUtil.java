package com.viktor.timeofftests.common.db;


import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public class DBUtil {

    public static void cleanDB(){
        try{
            log.info("Begin cleaning db tables");
            Connection connection = DbConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql = "truncate table \"BankHolidays\", \"Leaves\",  \"Companies\", \"DepartmentSupervisor\", \"Departments\", \"EmailAudit\", \"LeaveTypes\", \"Sessions\", \"UserFeeds\", \"Users\", \"schedule\", \"user_allowance_adjustment\";";
            statement.execute(sql);
        } catch (SQLException exception){
            exception.printStackTrace();
        }

    }
}