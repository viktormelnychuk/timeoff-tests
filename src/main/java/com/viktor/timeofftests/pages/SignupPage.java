package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage extends BasePage{
    private WebDriver driver;
    private Logger logger = LogManager.getLogger(SignupPage.class);

    private By alert = By.xpath("//div[@role='alert' and @class='alert alert-danger']");
    private By companyName = By.id("company_name_inp");
    private By firstName= By.id("name_inp");
    private By lastName = By.id("lastname_inp");
    private By email = By.id("email_inp");
    private By password = By.id("pass_inp");
    private By confirmPassword = By.id("confirm_pass_inp");
    private By country = By.id("country_inp");
    private By timezone = By.id("timezone_inp");
    private By createButton = By.id("submit_registration");

    SignupPage(WebDriver driver) {
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

    SignupPage fillCompanyName(String value){
        fillInputField(this.companyName, value);
        return this;
    }

    SignupPage fillFirstName(String value){
        fillInputField(this.firstName, value);
        return this;
    }

    SignupPage fillLastName(String value){
        fillInputField(this.lastName, value);
        return this;
    }

    SignupPage fillEmail(String value){
        fillInputField(this.email, value);
        return this;
    }

    SignupPage fillPassword(String value){
        fillInputField(this.password, value);
        return this;
    }

    SignupPage fillPasswordConfirmation(String value){
        fillInputField(this.confirmPassword, value);
        return this;
    }

    SignupPage selectCountry(String value) {
        selectOption(this.country, value);
        return this;
    }

    SignupPage selectTimeZone(String value){
        selectOption(this.timezone, value);
        return this;
    }

    CalendarPage clickCreateButtonExpectingSuccess(){
        clickButton(this.createButton);
        return new CalendarPage(this.driver);
    }

    SignupPage clickCreateButtonExpectingFailure(){
        clickButton(this.createButton);
        return this;
    }
    String getAlertMessage(){
        return findOne(this.alert).getText();
    }

    CalendarPage signupAsDefaultUser(){
        fillCompanyName(Constants.DEFAULT_COMPANY_NAME);
        fillEmail(Constants.DEFAULT_USER_EMAIL);
        fillFirstName(Constants.DEFAULT_USER_NAME);
        fillLastName(Constants.DEFAULT_USER_LAST_NAME);
        fillPassword(Constants.DEFAULT_USER_PASSWORD);
        fillPasswordConfirmation(Constants.DEFAULT_USER_PASSWORD);
        selectCountry(Constants.DEFAULT_COMPANY_COUNTRY);
        selectTimeZone(Constants.DEFAULT_COMPANY_TIMEZONE);
        return clickCreateButtonExpectingSuccess();
    }

    SignupPage signupWithUserExpectingFailure(User user){
        fillCompanyName("company");
        fillEmail(user.getEmail());
        fillFirstName(user.getName());
        fillLastName(user.getLastName());
        fillPassword("1234");
        fillPasswordConfirmation("1234");
        return clickCreateButtonExpectingFailure();
    }


    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
