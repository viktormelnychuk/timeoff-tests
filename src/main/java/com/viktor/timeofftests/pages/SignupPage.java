package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends BasePage{
    private String baseURL = "http://localhost:3000/register/";
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
    }
    public String getBaseURL() {
        return baseURL;
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
        this.createButton.click();
        return new CalendarPage(getDriver());
    }

    public SignupPage clickCreateButtonExpectingFailure(){
        this.createButton.click();
        return this;
    }


}