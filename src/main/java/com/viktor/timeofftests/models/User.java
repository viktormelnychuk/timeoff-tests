package com.viktor.timeofftests.models;

import com.viktor.timeofftests.services.CompanyService;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Date;

@Data
public class User {
    private String email;
    private String password;
    private String name;
    private String lastName;
    private boolean activated;
    private boolean admin;
    private boolean autoApprove;
    private Date startDate;
    private Date endDate;
    private int companyID;
    private int departmentID;

    private User(){}

    public static class Builder {
        private String email;
        private String password;
        private String name;
        private String lastName;
        private boolean activated = true;
        private boolean admin = false;
        private boolean autoApprove = true;
        private Date startDate = new Date();
        private Date endDate = new Date();
        private int companyID;
        private int departmentID;

        public Builder withEmail (String email){
            this.email = email;
            return this;
        }

        public Builder withPassword (String password){
            String pwf = password + "!2~`HswpPPLa22+=±§sdq qwe,appp qwwokDF_";
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
            // TODO: Add department here!
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

        public User build(){
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
            return user;
        }

    }

}
