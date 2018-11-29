package com.viktor.timeofftests.pages;

import org.openqa.selenium.WebDriver;

public class GeneralSettingsPage extends BasePage {
    private WebDriver driver;

    public GeneralSettingsPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public GeneralSettingsPage navigate(){
        LoginPage loginPage  = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithDefaultUser();
        driver.get(getBaseUrl());
        return this;
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/general/";
    }
    public String getPageTitle(){
        return findOne(pageTitle).getText();
    }


}
