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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
            log.info("Executing {}", statement.toString());
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
        log.info("Getting bank holidays fro company with id={}", companyID);
        Connection connection = DbConnection.getConnection();
        try {
            String sql = "SELECT * FROM \"BankHolidays\" WHERE \"companyId\"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyID);
            log.info("Executing ", statement);
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

    public List<BankHoliday> deserializeBankHolidays (ResultSet set){
        try {
            List<BankHoliday> result = new ArrayList<>();
            do {
                BankHoliday bankHoliday = new BankHoliday();
                bankHoliday.setName(set.getString("name"));
                Date resultDate = new Date(set.getDate("date").getTime());
                bankHoliday.setDate(resultDate);
                bankHoliday.setId(set.getInt("id"));
                bankHoliday.setCompanyId(set.getInt("companyId"));
                result.add(bankHoliday);
            } while ((set.next()));
            return result;
        } catch (Exception e){
            log.error("Error deserializing bank holidays", e);
            return new ArrayList<>();
        }
    }

    public List<BankHoliday> deserializeBankHolidays(List<WebElement> rows) throws ParseException {
        List<BankHoliday> result = new ArrayList<>();
        for (WebElement row : rows) {
            BankHoliday bankHoliday = new BankHoliday();
            WebElement inputDate = row.findElement(By.xpath(".//div[@class='input-append date']/input"));
            String dateFormat = inputDate.getAttribute("data-date-format");
            // Replace mm to MM to correctly parse date
            dateFormat = dateFormat.replaceAll("mm","MM");
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date date = format.parse(inputDate.getAttribute("value"));
            bankHoliday.setDate(date);
            bankHoliday.setName(row.findElement(By.xpath(".//div/input[contains(@name,'name')]")).getAttribute("value"));
            result.add(bankHoliday);
        }
        return result;
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
