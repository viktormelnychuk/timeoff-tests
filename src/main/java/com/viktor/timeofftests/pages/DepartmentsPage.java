package com.viktor.timeofftests.pages;

import org.openqa.selenium.WebDriver;

public class DepartmentsPage extends BasePage {
    private WebDriver driver;

    public DepartmentsPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public DepartmentsPage navigate(){
        LoginPage loginPage  = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithDefaultUser();
        driver.get(getBaseUrl());
        return new DepartmentsPage(driver);
    }
    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/";
    }

    public DepartmentsPage reload() {
        this.driver.navigate().refresh();
        return this;
    }
}
