package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.db.DbConnection;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Data
@Log4j2
public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private boolean activated;
    private boolean admin;
    private boolean autoApprove;
    private Timestamp startDate;
    private Timestamp endDate;
    private int companyID;
    private int departmentID;

    private User(){}

    @Log4j2
    public static class Builder {
        private String email;
        private String password;
        private String name;
        private String lastName;
        private boolean activated = true;
        private boolean admin = false;
        private boolean autoApprove = true;
        private Timestamp startDate = new Timestamp(new Date().getTime());
        private Timestamp endDate = new Timestamp(new Date().getTime());
        private int companyID;
        private int departmentID;

        public Builder withEmail (String email){
            this.email = email;
            return this;
        }

        public Builder withPassword (String password){
            String pwf = password + Constants.PASSWORD_HASH_SECRET;
            this.password = DigestUtils.md5Hex(pwf.getBytes());
            return this;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withLastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public Builder inCompany(Company company){
            this.companyID = company.getId();
            return this;
        }

        public Builder inCompany(String companyName){
            Company company = CompanyService.getInstance().getOrCreateCompanyWithName(companyName);
            this.companyID = company.getId();
            return this;
        }

        public Builder inDepartment(Department department){
            this.departmentID = department.getId();
            return this;
        }

        public Builder inDepartment (String departmentName){
            this.departmentID = DepartmentService.getInstance().getOrCreateDepartmentWithName(departmentName,this.companyID).getId();
            return this;
        }

        public Builder isAdmin(){
            this.admin = true;
            return this;
        }

        public Builder isDeactivated(){
            this.activated = false;
            return this;
        }

        public Builder isAutoApproved(){
            this.autoApprove = true;
            return this;
        }

        User build(){
            User user = new User();
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setName(this.name);
            user.setLastName(this.lastName);
            user.setActivated(this.activated);
            user.setAdmin(this.admin);
            user.setAutoApprove(this.autoApprove);
            user.setEndDate(this.endDate);
            user.setStartDate(this.startDate);
            user.setCompanyID(this.companyID);
            user.setDepartmentID(this.departmentID);
            return user;
        }


        public User buildAndStore(){
            User user = this.build();
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

    }

}
