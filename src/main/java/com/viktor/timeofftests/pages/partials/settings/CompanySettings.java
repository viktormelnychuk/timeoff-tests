package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CompanySettings extends BasePage {
    private WebDriver driver;

    private By companyNameInp = By.id("input_company_name");
    private By countrySelect = By.id("input_country");
    private By dateFormatSelect = By.id("input_date_format");
    private By timeZoneSelect = By.id("input_time_zone");
    private By companyLabel = By.xpath("//label[@for='input_company_name']");
    private By countryLabel = By.xpath("//label[@for='input_country']");
    private By dateFormatLabel = By.xpath("//label[@for='input_date_format']");
    private By timeZoneLabel = By.xpath("//label[@for='input_time_zone']");
    private By saveCompanySettingsButton = By.xpath("//form[@id='company_edit_form']//button[@type='submit']");

    public CompanySettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public CompanySettings setCompanyName(String value){
        fillInputField(companyNameInp, value);
        return this;
    }

    public String getCompanyName(){
        return getInputValue(companyNameInp);
    }

    public String getCountry(){
        return getSelectedOptionValue(countrySelect);
    }

    public String getDateFormat(){
        return getSelectedOption(dateFormatSelect);
    }

    public String getTimeZone(){
        return getSelectedOption(timeZoneSelect);
    }

    public String getCountryLabel(){
        return findOne(countryLabel).getText();
    }

    public String getCompanyLabel(){
        return findOne(companyLabel).getText();
    }

    public String getDateFormatLabel(){
        return findOne(dateFormatLabel).getText();
    }

    public String getTimeZoneLabel(){
        return findOne(timeZoneLabel).getText();
    }

    public CompanySettings setCompanyCountry (String companyCountry){
        selectOption(countrySelect,companyCountry);
        return this;
    }

    public CompanySettings setCompanyDateFormat (String companyDateFormat){
        selectOption(dateFormatSelect, companyDateFormat);
        return this;
    }

    public CompanySettings setCompanyTimeZone (String timeZone){
        selectOption(timeZoneSelect, timeZone);
        return this;
    }

    public GeneralSettingsPage saveCompanySettings(){
        clickButton(saveCompanySettingsButton);
        return new GeneralSettingsPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
