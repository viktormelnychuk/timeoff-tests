package com.viktor.timeofftests.services;

import com.viktor.timeofftests.db.DbConnection;
import com.viktor.timeofftests.models.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompanyService {
    private static CompanyService companyService;
    private static final Logger logger = LogManager.getLogger(CompanyService.class);
    public static CompanyService getInstance(){
        if(companyService == null){
            return new CompanyService();
        } else {
            return companyService;
        }
    }
    private CompanyService(){}

    public  Company getOrCreateCompanyWithName(String name){
        Connection connection = DbConnection.getConnection();
        String sql = "SELECT * FROM \"Companies\" WHERE name=? LIMIT 1;";
        try {
            PreparedStatement getCompanyStatement = connection.prepareStatement(sql);
            getCompanyStatement.setString(1,name);
            logger.info("Getting company with name '{}'", name);
            ResultSet resultSet = getCompanyStatement.executeQuery();
            if(resultSet.next()){
                logger.info("Found 1 company with id {}", resultSet.getString("id"));
                return deserializeComapny(resultSet);
            } else {
                logger.info("Company not found. Creating");
                return new Company.Builder()
                        .withName(name)
                        .buildAndSave();
            }
        } catch (Exception e){
            logger.error("Error retrieving company with name {}", name);
            return null;
        }
    }



    private Company deserializeComapny(ResultSet resultSet){
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
            logger.error("Error deserializing company", e);
            return null;
        }
    }
}
