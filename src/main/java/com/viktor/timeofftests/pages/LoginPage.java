package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private WebDriver driver;
    private By emailInput = By.id("email_inp");
    private By passwordInput = By.id("pass_inp");
    private By loginButton = By.id("submit_login");
    private By registerLink = By.linkText("Register new company");
    private By alertMessage = By.xpath("//div[@class='alert alert-danger' and @role='alert']");
    public LoginPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public String getBaseUrl() {
        return  "http://localhost:3000/login";
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

    public LoginPage clickLoginButtonExpectingFailure(){
        clickButton(loginButton);
        return this;
    }

    public String getAlertMessage (){
        return findOne(alertMessage).getText();
    }

    public CalendarPage loginWithDefaultUser(){
        fillEmail(Constants.DEFAULT_USER_EMAIL);
        fillPassword(Constants.DEFAULT_USER_PASSWORD);
        return clickLoginButtonExpectingSuccess();
    }

    public SignupPage clickRegisterLink() {
        clickButton(registerLink);
        return new SignupPage(this.driver);
    }
}
