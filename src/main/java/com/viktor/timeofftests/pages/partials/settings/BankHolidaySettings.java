package com.viktor.timeofftests.pages.partials.settings;

import com.github.javafaker.Faker;
import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewBankHolidayModal;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
public class BankHolidaySettings extends BasePage {

    private WebDriver driver;
    private By rows = By.xpath("//form[@id='update_bankholiday_form']//div[@class='input-append date']/../..");
    private String nameByIndexQuery = "//form[@id='update_bankholiday_form']//input[@name='name__%s']";
    private String deleteByttonByIndexQuery = "//form[@id='update_bankholiday_form']//button[@value='%s']";
    private By submitButton = By.xpath("//form[@id='update_bankholiday_form']//button[@type='submit']");
    private By addNewButton = By.id("add_new_bank_holiday_btn");
    private By importDefaultButton = By.id("bankholiday-import-btn");
    public BankHolidaySettings(WebDriver driver){
        super(driver);
        this.driver = driver;
    }


    public List<BankHoliday> getAllDisplayedHolidays(){
        List<WebElement> rowsList = findAllBy(rows);
        try {
            return deserializeBankHolidays(rowsList);
        } catch (ParseException e){
            log.error("Errro when getting bank holidays", e);
            return null;
        }
    }

    public String editHolidayByIndex(int index, String newName){
        By locator = By.xpath(String.format(nameByIndexQuery, index));
        fillInputField(locator, newName);
        return newName;
    }

    public String editHolidayNameWithRandomStringByIndex(int index){
        String rnd = new Faker().name().name();
        return editHolidayByIndex(index, rnd);
    }

    public Map<Integer, String> editMultipleHolidayNamesWithRandomString(int... indexes){
        Map<Integer, String> results = new HashMap<>();
        for (int index : indexes) {
            String newName = editHolidayNameWithRandomStringByIndex(index);
            results.put(index, newName);
        }
        return results;
    }

    public List<String> deleteMultipleHolidays(int... indexes){
        List<String> result = new ArrayList<>();
        for (int index : indexes) {
            By locator = By.xpath(String.format(deleteByttonByIndexQuery, index));
            By nameLocator = By.xpath(String.format(nameByIndexQuery, index));
            result.add(findOne(nameLocator).getAttribute("value"));
            clickButton(locator);
        }
        return result;
    }

    public void clickSubmitButton(){
        clickButton(submitButton);
    }

    public GeneralSettingsPage clickImportDefaultButton(){
        clickButton(importDefaultButton);
        return new GeneralSettingsPage(this.driver);
    }

    public AddNewBankHolidayModal clickAddNewButton(){
        clickButton(addNewButton);
        return new AddNewBankHolidayModal(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
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

}
