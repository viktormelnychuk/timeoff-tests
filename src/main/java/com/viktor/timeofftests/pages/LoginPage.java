package com.viktor.timeofftests.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private WebDriver driver;
    public LoginPage(WebDriver driver){
        super(driver);
    }

    public String getBaseUrl() {
        return  "http://localhost:3000/login/";
    }


}
