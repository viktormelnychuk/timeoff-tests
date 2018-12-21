package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewBankHolidayModal;
import com.viktor.timeofftests.services.BankHolidaysService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Log4j2
public class BankHolidaySettings extends BasePage {

    private WebDriver driver;
    private By rows = By.xpath("//form[@id='update_bankholiday_form']//div[@class='input-append date']/../..");
    private String nameByIndexQuery = "//form[@id='update_bankholiday_form']//input[@name='name__%s']";
    private String deleteByttonByIndexQuery = "//form[@id='update_bankholiday_form']//button[@value='%s']";
    private By submitButton = By.xpath("//form[@id='update_bankholiday_form']//button[@type='submit']");
    private By addNewButton = By.id("add_new_bank_holiday_btn");
    private By importDefaultButton = By.id("bankholiday-import-btn");
    @Getter
    private List<String> deletedHolidays = new LinkedList<>();
    public BankHolidaySettings(WebDriver driver){
        super(driver);
        this.driver = driver;
    }


//    public List<BankHoliday> getAllDisplayedHolidays(){
//        List<WebElement> rowsList = findAllBy(rows);
//        try {
//            return BankHolidaysService.getInstance().deserializeBankHolidays(rowsList);
//        } catch (ParseException e){
//            log.error("Errro when getting bank holidays", e);
//            return null;
//        }
//    }

    public BankHolidaySettings editHolidayByIndex(int index, String newName){
        By locator = By.xpath(String.format(nameByIndexQuery, index));
        fillInputField(locator, newName);
        return this;
    }

    public BankHolidaySettings editHolidayNameWithRandomStringByIndex(int index){
        String rnd = UUID.randomUUID().toString();
        return editHolidayByIndex(index, rnd);
    }

    public BankHolidaySettings editMultipleHolidayNamesWithRandomString(int[] indexes){
        for(int i = 0; i < indexes.length; i++){
            editHolidayNameWithRandomStringByIndex(indexes[i]);
        }
        return this;
    }

    public BankHolidaySettings deleteMultipleHolidays(int... indexes){
        for(int i = 0; i < indexes.length; i++){
            By locator = By.xpath(String.format(deleteByttonByIndexQuery,indexes[i]));
            By nameLocator = By.xpath(String.format(nameByIndexQuery,indexes[i]));
            deletedHolidays.add(findOne(nameLocator).getAttribute("value"));
            clickButton(locator);
        }
        return this;
    }

    public GeneralSettingsPage clickSubmitButton(){
        clickButton(submitButton);
        return new GeneralSettingsPage(this.driver);
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


}
