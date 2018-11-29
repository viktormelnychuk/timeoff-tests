package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.services.CompanyService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class Department {
    private int id;
    private String name;
    private int allowance;
    private boolean includePublicHolidays;
    private boolean isAccuredAllowance;
    private int companyId;
    private int bossId;

    private Department(){}

    @Log4j2
    public static class Builder {
        private int id;
        private String name = Constants.DEFAULT_DEPARTMENT_NAME;
        private int allowance = Constants.DEFAULT_DEPARTMENT_ALLOWANCE;
        private boolean includePublicHolidays;
        private boolean isAccuredAllowance;
        private int companyId;
        private int bossId;


        public Builder withName(String name){
            this.name = name;
            return  this;
        }

        public  Builder withAllowance (int allowance){
            this.allowance = allowance;
            return  this;
        }

        public Builder includePublicHolidays(boolean includePublicHolidays){
            this.includePublicHolidays = includePublicHolidays;
            return this;
        }

        public Builder isAccuredAllowance (boolean isAccuredAllowance){
            this.isAccuredAllowance = isAccuredAllowance;
            return this;
        }

        public Builder inCompany(String name){
            this.companyId = CompanyService.getInstance().getOrCreateCompanyWithName(name).getId();
            return this;
        }

        public Builder inCompany(int id){
            this.companyId = id;
            return this;
        }

        public Builder withAdminUserId(int id){
            this.bossId = id;
            return this;
        }

        public Department build(){
            Department department = new Department();
            department.name = this.name;
            department.allowance = this.allowance;
            department.companyId = this.companyId;
            department.includePublicHolidays = this.includePublicHolidays;
            department.isAccuredAllowance = this.isAccuredAllowance;
            department.bossId = this.bossId;
            return  department;
        }

    }
}
