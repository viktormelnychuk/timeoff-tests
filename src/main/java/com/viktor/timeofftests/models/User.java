package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;

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
    private Timestamp startDate;
    private Timestamp endDate;
    private int companyID;
    private int departmentID;

    private User(){}

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
        private boolean autoApprove = true;
        private Timestamp startDate = new Timestamp(new Date().getTime());
        private Timestamp endDate;
        private int companyID;
        private int departmentID;

        public Builder withEmail (String email){
            this.email = email;
            return this;
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

        public Builder startedOn (Date date){
            this.startDate = new Timestamp(date.getTime());
            return this;
        }

        public Builder endedOn (Date date){
            this.endDate = new Timestamp(date.getTime());
            return this;
        }

        public User build(){
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
