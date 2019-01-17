package com.viktor.timeofftests.services;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import lombok.extern.log4j.Log4j2;

import java.sql.*;

@Log4j2
public class CompanyService {
    private LeaveTypeService leaveTypeService;
    private BankHolidaysService bankHolidayService;
    private ScheduleService scheduleService;

    public CompanyService(LeaveTypeService leaveTypeService, BankHolidaysService bankHolidayService,
                          ScheduleService scheduleService){
        this.leaveTypeService = leaveTypeService;
        this.bankHolidayService = bankHolidayService;
        this.scheduleService = scheduleService;
    }

    public  Company getOrCreateCompanyWithName(String name){
        Company company = getCompanyWithName(name);
        if (company == null) {
            Company newCompany = new Company.Builder()
                    .withName(name)
                    .build();
            return saveCompany(newCompany);
        } else {
            return company;
        }
    }
    public Company getOneExistingCompany() {
        Connection connection = DbConnection.getConnection();
        log.debug("Getting one existing company");
        try{
            String sql = "SELECT * FROM \"Companies\"";
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            set.last();
            if(set.getRow() > 1){
                throw new Exception("More than one company was found. Please set companyId explicitly");
            }
            set.first();
            return deserializeCompany(set);
        } catch (Exception e){
            log.error("Error getting companies", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Company getCompanyForDepartmentWithId(int departmentId){
        Connection connection = DbConnection.getConnection();
        log.debug("Getting company of department[id={}]", departmentId);
        try{
            String sql = "SELECT * FROM \"Companies\" WHERE id=(SELECT \"companyId\" FROM \"Departments\" WHERE id=?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, departmentId);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeCompany(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error getting company", e);
            return null;
        }
    }

    public Company getCompanyWithName (String name){
        log.debug("Getting company with name={}", name);
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Companies\" WHERE name=? LIMIT 1;";
        try {
            PreparedStatement getCompany = connection.prepareStatement(sql);
            getCompany.setString(1, name);
            log.debug("Executing {}", getCompany);
            ResultSet set = getCompany.executeQuery();
            if(set.next()){
                return deserializeCompany(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error retrieving company with name {}", name);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Company getCompanyWithId (int id){
        log.debug("Getting company with id={}", id);
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Companies\" WHERE id=?;";
        try {
            PreparedStatement getCompany = connection.prepareStatement(sql);
            getCompany.setInt(1, id);
            log.debug("Executing {}", getCompany);
            ResultSet set = getCompany.executeQuery();
            if (set.next()){
                return deserializeCompany(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error getting company with", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public Company saveCompany (Company company){
        log.info("Prepare to save company with name=\""+company.getName()+ "\"");
        Connection connection = DbConnection.getConnection();
        String sql = new StringBuilder().append("INSERT INTO \"Companies\" (name, country, start_of_new_year, share_all_absences, ldap_auth_enabled, ldap_auth_config, date_format, company_wide_message, mode, timezone,\"createdAt\",\"updatedAt\")")
                .append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);").toString();
        try {
            PreparedStatement createCompany = connection.prepareStatement(sql);
            createCompany.setString(1,company.getName());
            createCompany.setString(2,company.getCountry());
            createCompany.setInt(3, company.getStartOfNewYear());
            createCompany.setBoolean(4, company.isShareAllAbsences());
            createCompany.setBoolean(5,company.isLdapAuthEnabled());
            createCompany.setString(6, company.getLdapAuthConfig());
            createCompany.setString(7,company.getDateFormat());
            createCompany.setString(8, company.getCompanyWideMessage());
            createCompany.setInt(9,company.getMode());
            createCompany.setString(10,company.getTimezone());
            createCompany.setTimestamp(11,new Timestamp(new java.util.Date().getTime()));
            createCompany.setTimestamp(12,new Timestamp(new java.util.Date().getTime()));
            log.debug("Executing: "+createCompany.toString());
            createCompany.executeUpdate();
            log.info("Saved company with name=\""+company.getName()+ "\"");
            log.debug("Getting id of company with name=\""+company.getName()+ "\"");

            String getCompanySql = "SELECT \"Companies\".id FROM \"Companies\" WHERE \"Companies\".name = ?;";
            PreparedStatement getCompany = connection.prepareStatement(getCompanySql);
            getCompany.setString(1, company.getName());
            log.debug("Executing: "+getCompany.toString());
            ResultSet resultSet = getCompany.executeQuery();
            resultSet.next();
            company.setId(resultSet.getInt(1));
            if (company.getLeaveTypes() != null){
                leaveTypeService.insertLeaveTypes(company.getId(),company.getLeaveTypes());
            } else {
                leaveTypeService.insertLeaveTypes(company.getId(),Constants.DEFAULT_LEAVE_TYPES);
            }
            bankHolidayService.populateBankHolidaysForCompany(company);
            scheduleService.insertDefaultSchedule(company.getId());
            log.info("Company ID id '"+company.getId()+"'");
            return company;
        } catch (Exception e){
            log.error("Error when creating company",e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }

    }

    Company deserializeCompany(ResultSet resultSet){
        try {
            return new Company.Builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .withCountry(resultSet.getString("country"))
                    .withStartOfNewYear(resultSet.getInt("start_of_new_year"))
                    .shareAllAbsences(resultSet.getBoolean("share_all_absences"))
                    .ldapAuthEnabled(resultSet.getBoolean("ldap_auth_enabled"))
                    .ldapAuthConfig(resultSet.getString("ldap_auth_config"))
                    .withDateFormat(resultSet.getString("date_format"))
                    .withCompanyWideMessage(resultSet.getString("company_wide_message"))
                    .withMode(resultSet.getInt("mode"))
                    .withTimeZone(resultSet.getString("timezone"))
                    .build();

        } catch (Exception e){
            log.error("Error deserializing company", e);
            return null;
        }
    }
}
