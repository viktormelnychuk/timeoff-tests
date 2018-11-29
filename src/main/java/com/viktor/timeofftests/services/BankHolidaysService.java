package com.viktor.timeofftests.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.models.Company;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;


@Log4j2
public class BankHolidaysService {
    private static BankHolidaysService bankHolidaysService;
    private CompanyService companyService = CompanyService.getInstance();
    public static BankHolidaysService getInstance(){
        if(bankHolidaysService == null){
            return new BankHolidaysService();
        } else {
            return bankHolidaysService;
        }
    }
    private BankHolidaysService(){}

    void populateBankHolidaysForCompany(String companyName){
        log.info("Inserting default holidays for company with name={}", companyName);
        Company company = companyService.getCompanyWithName(companyName);
        BankHoliday[] bankHolidays = getHolidaysForCountry(company.getCountry());
        String sql = "INSERT INTO \"BankHolidays\" (name, date, \"createdAt\", \"updatedAt\", \"companyId\") VALUES(?, ?, ?, ?, ?)";
        try{
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            for (BankHoliday bankHoliday : bankHolidays) {
                statement.setString(1, bankHoliday.getName());
                statement.setTimestamp(2, new Timestamp(bankHoliday.getDate().getTime()));
                statement.setTimestamp(3, new Timestamp(new Date().getTime()));
                statement.setTimestamp(4, new Timestamp(new Date().getTime()));
                statement.setInt(5, company.getId());
                statement.addBatch();
            }
            log.info("Executing {}", statement.toString());
            int[] rowsAffected = statement.executeBatch();
            if (rowsAffected.length != bankHolidays.length){
                throw new Exception("Not all bank holidays were inserted");
            }
        } catch (Exception e){
            log.error("Error inserting bank holidays", e);
        }
    }

    private BankHoliday[] getHolidaysForCountry(String countryCode){
        try {
            log.info("Reading bank holidays from [localisation.json] for country={}",countryCode);
            File jsonFile = new File(getClass().getClassLoader().getResource("localisation.json").getFile());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonFile);
            JsonNode country = root.get("countries").get(countryCode);
            JsonNode countryHolidays = country.get("bank_holidays");
            return mapper.readValue(countryHolidays.toString(), BankHoliday[].class);
        } catch (Exception exception){
            log.error("Error reading from [localisation.json]",exception);
            return null;
        }
    }
}
