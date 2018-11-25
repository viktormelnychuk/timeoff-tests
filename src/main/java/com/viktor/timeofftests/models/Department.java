package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.services.CompanyService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

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

    public void assignBossUserId(int id){
        Connection connection = DbConnection.getConnection();
        String sql = "UPDATE \"Departments\" SET \"bossId\"=?, \"updatedAt\"=? WHERE \"id\"=?";
        try{
            PreparedStatement updateDepartmentWithBossId = connection.prepareStatement(sql);
            updateDepartmentWithBossId.setInt(1,id);
            updateDepartmentWithBossId.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            updateDepartmentWithBossId.setInt(3, this.getId());
            log.info("Updating department with id={} to have bossId={}", this.getId(), id);
            log.info("Executing {}",updateDepartmentWithBossId.toString());
            updateDepartmentWithBossId.executeUpdate();

        } catch (Exception e){
            log.error("Error updating department", e);
        }
    }
}
