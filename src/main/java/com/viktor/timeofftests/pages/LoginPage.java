package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    private WebDriver driver;

    @FindBy(id= "email_inp")
    private WebElement emailInput;

    @FindBy(id= "pass_inp")
    private WebElement passwordInput;

    @FindBy(id="submit_login")
    private WebElement loginButton;

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
