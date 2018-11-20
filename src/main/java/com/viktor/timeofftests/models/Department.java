package com.viktor.timeofftests.models;

import com.viktor.timeofftests.services.CompanyService;

public class Department {
    private int id;
    private String name;
    private int allowance;
    private boolean includePublicHolidays;
    private boolean isAccuredAllowance;
    private int companyId;
    private int bossId;

    private Department(){}

    public static class Builder {
        private int id;
        private String name;
        private int allowance;
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

        public Builder inCompany(String name){
            this.companyId = CompanyService.getInstance().getOrCreateCompanyWithName(name).getId();
            return this;
        }

        public Builder withAdminUserId(int id){
            this.bossId = id;
            return this;
        }

        public Department buildAndStore(){
            // TODO: Finish this!!!!!
            Department department = new Department();
            return department;
        }

    }
}
