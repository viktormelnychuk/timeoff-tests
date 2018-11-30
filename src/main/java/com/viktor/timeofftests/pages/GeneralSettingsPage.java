package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GeneralSettingsPage extends BasePage {
    private WebDriver driver;

    private By companyNameInp = By.id("input_company_name");
    private By countrySelect = By.id("input_country");
    private By dateFormatSelect = By.id("input_date_format");
    private By timeZoneSelect = By.id("input_time_zone");
    private By saveCompanySettingsButton = By.xpath("//form[@id='company_edit_form']//button[@type='submit']");
    private By alert = By.xpath("//div[@role='alert']");
    public GeneralSettingsPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    void navigate(){
        LoginPage loginPage  = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithDefaultUser();
        driver.get(getBaseUrl());
    }

    GeneralSettingsPage setCompanyName(String value){
        fillInputField(companyNameInp, value);
        return this;
    }

    GeneralSettingsPage setCompanyCountry (String companyCountry){
        selectOption(countrySelect,companyCountry);
        return this;
    }

    GeneralSettingsPage setCompanyDateFormat (String companyDateFormat){
        selectOption(dateFormatSelect, companyDateFormat);
        return this;
    }

    GeneralSettingsPage setCompanyTimeZone (String timeZone){
        selectOption(timeZoneSelect, timeZone);
        return this;
    }

    GeneralSettingsPage saveCompanySettings(){
        clickButton(saveCompanySettingsButton);
        return this;
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/general/";
    }
    public String getPageTitle(){
        return findOne(pageTitle).getText();
    }
    public String getAlertText(){return findOne(alert).getText();}


}
