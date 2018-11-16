package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends PageObject{
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


    private Select country;
    private Select timezone;

    @FindBy(id="submit_registration")
    private WebElement createButton;

    public SignupPage(WebDriver driver) {
        super(driver);
        if (!this.driver.getCurrentUrl().equalsIgnoreCase(baseURL)) {
            this.driver.get(baseURL);
        }
        this.country = new Select(this.driver.findElement(By.id("country_inp")));
        this.timezone = new Select(this.driver.findElement(By.id("timezone_inp")));
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

    public SignupPage selectCountry(String value) throws Exception {
        selectOption (this.country, value);
        return this;
    }

    public SignupPage selectTimeZone(String value) throws Exception {
        selectOption(this.timezone, value);
        return this;
    }

    public CalendarPage clickCreateButtonExpectingSuccess(){
        this.createButton.click();
        return new CalendarPage(this.driver);
    }

    public SignupPage clickCreateButtonExpectingFailure(){
        this.createButton.click();
        return this;
    }


}
