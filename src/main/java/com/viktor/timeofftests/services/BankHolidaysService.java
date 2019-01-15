package com.viktor.timeofftests.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.timeofftests.common.db.DBUtil;
import com.viktor.timeofftests.common.db.DbConnection;
import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.models.Company;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Log4j2
public class BankHolidaysService {

    public BankHolidaysService(){
    }

    void populateBankHolidaysForCompany(Company company){
        log.debug("Inserting default holidays for company with id={}", company.getId());
        Connection connection = DbConnection.getConnection();
        BankHoliday[] bankHolidays = getHolidaysForCountry(company.getCountry());
        String sql = "INSERT INTO \"BankHolidays\" (name, date, \"createdAt\", \"updatedAt\", \"companyId\") VALUES(?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            for (BankHoliday bankHoliday : bankHolidays) {
                statement.setString(1, bankHoliday.getName());
                statement.setTimestamp(2, new Timestamp(bankHoliday.getDate().getTime()));
                statement.setTimestamp(3, new Timestamp(new Date().getTime()));
                statement.setTimestamp(4, new Timestamp(new Date().getTime()));
                statement.setInt(5, company.getId());
                statement.addBatch();
            }
            log.debug("Executing {}", statement.toString());
            int[] rowsAffected = statement.executeBatch();
            if (rowsAffected.length != bankHolidays.length){
                throw new Exception("Not all bank holidays were inserted");
            }
        } catch (Exception e){
            log.error("Error inserting bank holidays", e);
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public List<BankHoliday> getAllBankHolidaysForCompany(int companyID){
        log.debug("Getting bank holidays fro company with id={}", companyID);
        Connection connection = DbConnection.getConnection();
        try {
            String sql = "SELECT * FROM \"BankHolidays\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyID);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeBankHolidays(set);
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e){
            log.error("Error getting holidays for company with id={}", companyID, e);
            return new ArrayList<>();
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public BankHoliday getWithNameForCompany(String name, int id) {
        log.debug("Getting bank holiday wit name={}", name);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"BankHolidays\" WHERE name=? AND \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, id);
            log.debug("Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeBankHoliday(set);
            } else {
                return null;
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return null;
        } finally {
            DBUtil.closeConnection(connection);
        }
    }

    public List<BankHoliday> deserializeBankHolidays (ResultSet set){
        try {
            List<BankHoliday> result = new ArrayList<>();
            do {
                result.add(deserializeBankHoliday(set));
            } while ((set.next()));
            return result;
        } catch (Exception e){
            log.error("Error deserializing bank holidays", e);
            return new ArrayList<>();
        }
    }
    public BankHoliday deserializeBankHoliday(ResultSet set){
        try {
            BankHoliday bankHoliday = new BankHoliday();
            bankHoliday.setName(set.getString("name"));
            Date resultDate = new Date(set.getDate("date").getTime());
            bankHoliday.setDate(resultDate);
            bankHoliday.setId(set.getInt("id"));
            bankHoliday.setCompanyId(set.getInt("companyId"));
            return bankHoliday;
        } catch (Exception e){
            log.error("Error deserializing bank holiday", e);
            return null;
        }
    }

    private BankHoliday[] getHolidaysForCountry(String countryCode){
        try {
            log.debug("Reading bank holidays from [localisation.json] for country={}",countryCode);
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

    public boolean isHoliday(LocalDate date, int userId) {
        boolean result = false;
        List<BankHoliday> holidays = getBankHolidaysForUserWithId(userId);
        for (BankHoliday holiday : holidays) {
            // convert to holiday date to LocalDate
            LocalDate holidayDate = holiday.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(holidayDate.isEqual(date)){
                result = true;
            }
        }
        return result;
    }

    private List<BankHoliday> getBankHolidaysForUserWithId(int userId) {
        log.debug("Getting bank holidays for company user with id=[{}] belongs", userId);
        Connection connection = DbConnection.getConnection();
        try{
            String sql = "SELECT * FROM \"BankHolidays\" WHERE \"companyId\"=" +
                    "(SELECT \"companyId\" FROM \"Users\" WHERE \"Users\".id=?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            log.debug( "Executing {}", statement);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return deserializeBankHolidays(set);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e){
            log.error("Error occurred", e);
            return Collections.emptyList();
        } finally {
            DBUtil.closeConnection(connection);
        }
    }
}
