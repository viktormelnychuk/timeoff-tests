package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.services.CompanyService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Data
@Log4j2
public class Department {
    private int id;
    private String name;
    private double allowance;
    private boolean includePublicHolidays;
    private boolean isAccuredAllowance;
    private int companyId;
    private int bossId;

    public Department(){}

    @Log4j2
    public static class Builder {
        private int id;
        private String name = Constants.DEFAULT_DEPARTMENT_NAME;
        private int allowance = Constants.DEFAULT_DEPARTMENT_ALLOWANCE;
        private boolean includePublicHolidays;
        private boolean isAccuredAllowance;
        private int companyId;
        private int bossId;

        public Builder(){}

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

    @Override
    public boolean equals (Object o){
        boolean result = true;
        Department toCompare = (Department) o;
        if(!Objects.equals(toCompare.getName(), this.getName())){
            result = false;
        }
        if(toCompare.getAllowance() != this.getAllowance()){
            result = false;
        }
        if(toCompare.isIncludePublicHolidays() != this.isIncludePublicHolidays()){
            result = false;
        }
        if(toCompare.isAccuredAllowance() != this.isAccuredAllowance()){
            result = false;
        }
        if(toCompare.getId() != this.getId()){
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode(){
        int result = 10000000;
        result += this.getName().hashCode();
        result += this.getAllowance();
        if(this.isAccuredAllowance()){
            result += 1;
        } else {
            result += 0;
        }
        if(this.isIncludePublicHolidays()){
            result += 1;
        } else {
            result += 0;
        }
        result += this.getId();
        result += this.getCompanyId();
        result += this.getBossId();
        return result;
    }
}
