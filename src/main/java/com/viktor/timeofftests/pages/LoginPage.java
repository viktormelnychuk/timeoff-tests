package com.viktor.timeofftests.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage {
    private String baseUrl = "http://localhost:3000/login/";
    private WebDriver driver;
    public LoginPage(WebDriver driver){
        this.driver = driver;
    }


}
