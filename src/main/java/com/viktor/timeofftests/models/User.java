package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Data
@Log4j2
public class User {
    private int id;
    private String email;
    private String password;
    private String rawPassword;
    private String name;
    private String lastName;
    private boolean activated;
    private boolean admin;
    private boolean autoApprove;
    private LocalDate startDate;
    private LocalDate endDate;
    private int companyID;
    private int departmentID;
    private String departmentName;
    private String companyName;

    public User(){}

    public String getFullName(){
        return this.name + " " + this.lastName;
    }

    @Log4j2
    public static class Builder {
        private String email = Constants.DEFAULT_USER_EMAIL;
        // hashed raw password
        private String password = "2df81af5c193524b83db263efb5db2a2";
        private String rawPassword = Constants.DEFAULT_USER_PASSWORD;
        private String name = Constants.DEFAULT_USER_NAME;
        private String lastName = Constants.DEFAULT_USER_LAST_NAME;
        private boolean activated = true;
        private boolean admin = false;
        private boolean autoApprove = false;
        private LocalDate startDate = LocalDate.now();
        private LocalDate endDate;
        private int companyID;
        private int departmentID;

        public Builder withEmail (String email){
            this.email = email;
            return this;
        }

        public Builder(){
        }

        public Builder withPassword (String password){
            this.rawPassword = password;
            String pwf = password + Constants.PASSWORD_HASH_SECRET;
            this.password = DigestUtils.md5Hex(pwf.getBytes(StandardCharsets.UTF_8));
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

        public Builder inCompany(int companyID){
            this.companyID = companyID;
            return this;
        }

        public Builder inDepartment(Department department){
            this.departmentID = department.getId();
            return this;
        }

        public Builder inDepartment (int departmentID){
            this.departmentID = departmentID;
            return this;
        }

        public Builder isAdmin(){
            this.admin = true;
            return this;
        }

        public Builder admin(boolean b){
            this.admin = b;
            return this;
        }

        public Builder isDeactivated(){
            this.activated = false;
            return this;
        }

        public Builder activated(boolean b){
            this.activated = b;
            return this;
        }

        public Builder autoApproved(boolean b){
            this.autoApprove = b;
            return this;
        }

        public Builder isAutoApproved(){
            this.autoApprove = true;
            return this;
        }

        public Builder startedOn (LocalDate date){
            if(date == null){
                this.startDate = LocalDate.now();
                return this;
            }
            this.startDate = date;
            return this;
        }


        public Builder endedOn (LocalDate date){
            if(date == null){
                this.endDate = null;
                return this;
            }
            this.endDate = date;
            return this;
        }

        public User build(){
            if(this.startDate == null){
                this.startDate = LocalDate.now();
            }
            User user = new User();
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setRawPassword(this.rawPassword);
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
    }

}
