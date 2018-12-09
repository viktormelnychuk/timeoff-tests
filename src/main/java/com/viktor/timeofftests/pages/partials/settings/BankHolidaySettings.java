package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.services.BankHolidaysService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Log4j2
public class BankHolidaySettings extends BasePage {

    private WebDriver driver;
    private By rows = By.xpath("//form[@id='update_bankholiday_form']//div[@class='input-append date']/../..");
    private String nameByIndexQuery = "//form[@id='update_bankholiday_form']//input[@name='name__%s']";
    private By submitButton = By.xpath("//form[@id='update_bankholiday_form']//button[@type='submit']");

    public BankHolidaySettings(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<BankHoliday> getAllDisplayedHolidays(){
        List<WebElement> rowsList = findAllBy(rows);
        try {
            return BankHolidaysService.getInstance().deserializeBankHolidays(rowsList);
        } catch (ParseException e){
            log.error("Errro when getting bank holidays", e);
            return null;
        }
    }

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
            editHolidayNameWithRandomStringByIndex(i);
        }
        return this;
    }

    public GeneralSettingsPage clickSubmitButton(){
        clickButton(submitButton);
        return new GeneralSettingsPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }


}
