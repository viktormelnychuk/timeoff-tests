package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    private WebDriver driver;
    private By emailInput = By.id("email_inp");
    private By passwordInput = By.id("pass_inp");
    private By loginButton = By.id("submit_login");

    public LoginPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public String getBaseUrl() {
        return  "http://localhost:3000/login/";
    }

    public LoginPage fillEmail(String value){
        fillInputField(this.emailInput, value);
        return this;
    }

    public LoginPage fillPassword (String value){
        fillInputField(this.passwordInput, value);
        return this;
    }

    public CalendarPage clickLoginButtonExpectingSuccess(){
        clickButton(loginButton);
        return new CalendarPage(this.driver);
    }

    public CalendarPage loginWithDefaultUser(){
        fillEmail(Constants.DEFAULT_USER_EMAIL);
        fillPassword(Constants.DEFAULT_USER_PASSWORD);
        return clickLoginButtonExpectingSuccess();
    }

}
