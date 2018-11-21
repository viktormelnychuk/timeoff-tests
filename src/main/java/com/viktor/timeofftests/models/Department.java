package com.viktor.timeofftests.models;

import com.viktor.timeofftests.db.DbConnection;
import com.viktor.timeofftests.services.CompanyService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        public Department buildAndStore(){
            Department department = this.build();
            Connection connection = DbConnection.getConnection();
            String sql = "INSERT INTO \"Departments\" (name, allowance, include_public_holidays, is_accrued_allowance, \"createdAt\", \"updatedAt\", \"companyId\", \"bossId\") VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            try{
                PreparedStatement insertDepartment = connection.prepareStatement(sql);
                insertDepartment.setString(1, department.getName());
                insertDepartment.setInt(2, department.getAllowance());
                insertDepartment.setBoolean(3,department.isIncludePublicHolidays());
                insertDepartment.setBoolean(4,department.isAccuredAllowance());
                insertDepartment.setTimestamp(5,new Timestamp(new java.util.Date().getTime()));
                insertDepartment.setTimestamp(6,new Timestamp(new java.util.Date().getTime()));
                insertDepartment.setInt(7, department.getCompanyId());
                insertDepartment.setInt(8, department.getBossId());
                log.info("Executing {}",insertDepartment.toString());
                insertDepartment.executeUpdate();

                log.info("Getting id of department with name {}",department.getName());

                String selectSql = "SELECT id FROM \"Departments\" WHERE  \"Departments\".name = ? LIMIT 1";
                PreparedStatement selectDep = connection.prepareStatement(selectSql);
                selectDep.setString(1,department.getName());
                log.info("Executing {}", selectDep.toString());
                ResultSet set = selectDep.executeQuery();
                set.next();
                department.setId(set.getInt("id"));
                return department;

            } catch (Exception e){
                log.error("Error inserting department", e);
                return null;
            }
        }

    }

    public void assignBoosUserId(int id){
        //  Executing (default): UPDATE "Departments" SET "bossId"=4,"updatedAt"='2018-11-20 19:04:43.380 +00:00' WHERE "id" = 4
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
