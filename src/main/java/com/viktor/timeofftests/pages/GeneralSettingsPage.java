package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GeneralSettingsPage extends BasePage {
    private WebDriver driver;
    public CompanySettings companySettings;
    public CompanyScheduleSettings companyScheduleSettings;
    private By alert = By.xpath("//div[@role='alert']");
    public GeneralSettingsPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        this.companySettings = new CompanySettings(driver);
        this.companyScheduleSettings = new CompanyScheduleSettings(driver);
    }

    void navigate(){
        LoginPage loginPage  = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithDefaultUser();
        driver.get(getBaseUrl());
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
