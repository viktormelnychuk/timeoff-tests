package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewBankHolidayModal extends BasePage {
    private WebDriver driver;

    private By nameInput = By.id("bank_holiday_name_new");
    private By dateInput = By.id("bank_holiday_date_new");
    private By createButton = By.xpath("//form[@id='add_new_bank_holiday_form']//button[@type='submit']");
    public AddNewBankHolidayModal(WebDriver driver){
        super(driver);
        this.driver = driver;
    }


    public AddNewBankHolidayModal fillName(String name){
        fillInputField(nameInput, name);
        return this;
    }

    public AddNewBankHolidayModal fillDate(Date date){
        WebElement inputElement = findOne(dateInput);
        String dateFormat = inputElement.getAttribute("data-date-format").replaceAll("mm","MM");
        DateFormat format = new SimpleDateFormat(dateFormat);
        String dateToInsert = format.format(date);
        fillInputField(dateInput, dateToInsert);
        return this;
    }

    public GeneralSettingsPage clickCreateButton(){
        clickButton(createButton);
        return new GeneralSettingsPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
