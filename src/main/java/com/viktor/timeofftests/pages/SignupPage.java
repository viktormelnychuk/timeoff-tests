package com.viktor.timeofftests.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends BasePage{
    private WebDriver driver;
    private Logger logger = LogManager.getLogger(SignupPage.class);
    @FindBy(id="company_name_inp")
    private WebElement companyName;

    @FindBy(id="name_inp")
    private WebElement firstName;

    @FindBy(id="lastname_inp")
    private WebElement lastName;

    @FindBy(id="email_inp")
    private WebElement email;

    @FindBy(id="pass_inp")
    private WebElement password;

    @FindBy(id="confirm_pass_inp")
    private WebElement confirmPassword;

    @FindBy(id="country_inp")
    private WebElement country;

    @FindBy(id="timezone_inp")
    private WebElement timezone;

    @FindBy(id="submit_registration")
    private WebElement createButton;

    public SignupPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public String getBaseUrl() {
        return "http://localhost:3000/register/";
    }
    @Override
    public void open(String url){
        super.open(url);
    }

    public SignupPage fillCompanyName(String value){
        fillInputField(this.companyName, value);
        return this;
    }

    public SignupPage fillFirstName(String value){
        fillInputField(this.firstName, value);
        return this;
    }

    public SignupPage fillLastName(String value){
        fillInputField(this.lastName, value);
        return this;
    }

    public SignupPage fillEmail(String value){
        fillInputField(this.email, value);
        return this;
    }

    public SignupPage fillPassword (String value){
        fillInputField(this.password, value);
        return this;
    }

    public SignupPage fillPasswordConfirmation(String value){
        fillInputField(this.confirmPassword, value);
        return this;
    }

    public SignupPage selectCountry(String value) {
        selectOption(this.country, value);
        return this;
    }

    public SignupPage selectTimeZone(String value){
        selectOption(this.timezone, value);
        return this;
    }

    public CalendarPage clickCreateButtonExpectingSuccess(){
        clickButton(this.createButton);
        return new CalendarPage(getDriver());
    }

    public SignupPage clickCreateButtonExpectingFailure(){
        clickButton(this.createButton);
        return this;
    }


    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
