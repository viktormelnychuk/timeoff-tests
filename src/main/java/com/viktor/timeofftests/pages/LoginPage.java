package com.viktor.timeofftests.pages;

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

    public void fillEmail(String value){
        fillInputField(this.emailInput, value);
    }

    public void fillPassword (String value){
        fillInputField(this.passwordInput, value);
    }

    public CalendarPage clickLoginButton(){
        clickButton(loginButton);
        return new CalendarPage(this.driver);
    }
}
