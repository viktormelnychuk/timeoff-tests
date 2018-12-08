package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.services.BankHolidaysService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.util.List;

@Log4j2
public class BankHolidaySettings extends BasePage {

    private WebDriver driver;
    private By rows = By.xpath("//form[@id='update_bankholiday_form']//div[@class='input-append date']/../..");
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

    @Override
    public String getBaseUrl() {
        return null;
    }


}
