package com.viktor.timeofftests.models;

import com.viktor.timeofftests.db.DbConnection;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

@Data
public class Company {
    private int id;
    private String name;
    private String country;
    private int startOfNewYear;
    private boolean shareAllAbsences;
    private boolean ldapAuthEnabled;
    private String ldapAuthConfig;
    private String dateFormat;
    private String companyWideMessage;
    private int mode;
    private String timezone;

    private Company(){}

    public static class Builder{
        Logger logger = LogManager.getLogger(Company.Builder.class);
        private int id;
        private String name;
        private String country;
        private int startOfNewYear;
        private boolean shareAllAbsences;
        private boolean ldapAuthEnabled;
        private String ldapAuthConfig;
        private String dateFormat;
        private String companyWideMessage;
        private int mode;
        private String timezone;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withCountry(String country){
            this.country = country;
            return this;
        }

        public Builder withStartOfNewYear(int startOfNewYear){
            this.startOfNewYear = startOfNewYear;
            return this;
        }

        public Builder shareAllAbsences(boolean shareAllAbsences){
            this.shareAllAbsences = shareAllAbsences;
            return this;
        }
        public Builder ldapAuthEnabled(boolean ldapAuthEnabled){
            this.ldapAuthEnabled = ldapAuthEnabled;
            return this;
        }

        public Builder ldapAuthConfig (String config){
            this.ldapAuthConfig = config;
            return this;
        }
        public Builder withDateFormat(String dateFormat){
            this.dateFormat = dateFormat;
            return this;
        }

        public Builder withCompanyWideMessage (String message){
            this.companyWideMessage = message;
            return this;
        }

        public Builder withMode(int mode){
            this.mode = mode;
            return this;
        }

        public Builder withTimeZone(String timezone){
            this.timezone = timezone;
            return this;
        }

        public Company build(){
            Company company = new Company();
            company.name = this.name;
            company.country = this.country;
            company.startOfNewYear = this.startOfNewYear;
            company.shareAllAbsences = this.shareAllAbsences;
            company.ldapAuthEnabled = this.ldapAuthEnabled;
            company.ldapAuthConfig = this.ldapAuthConfig;
            company.dateFormat = this.dateFormat;
            company.companyWideMessage = this.companyWideMessage;
            company.mode = this.mode;
            company.timezone = this.timezone;
            return company;
        }

        public Company buildAndSave(){
            Company company = this.build();
            logger.info("Prepare to save company with name=\""+company.getName()+ "\"");
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
                createCompany.setString(10,company.timezone);
                createCompany.setTimestamp(11,new Timestamp(new java.util.Date().getTime()));
                createCompany.setTimestamp(12,new Timestamp(new java.util.Date().getTime()));
                logger.info("Executing: "+createCompany.toString());
                createCompany.executeUpdate();
                logger.info("Saved company with name=\""+company.getName()+ "\"");
                logger.info("Getting id of company with name=\""+company.getName()+ "\"");

                String getCompanySql = "SELECT \"Companies\".id FROM \"Companies\" WHERE \"Companies\".name = ?;";
                PreparedStatement getCompany = connection.prepareStatement(getCompanySql);
                getCompany.setString(1, company.getName());
                logger.info("Executing: "+getCompany.toString());
                ResultSet resultSet = getCompany.executeQuery();
                resultSet.next();
                company.setId(resultSet.getInt(1));
                logger.info("Company ID id '"+company.getId()+"'");
                return company;
            } catch (Exception e){
                logger.error("Error when creating company",e);
                return null;
            }

        }
    }
}