package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.UserService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage extends BasePage{
    private WebDriver driver;
    private UserService userService = UserService.getInstance();
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

    public SignupPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public String getBaseUrl() {
        return "http://localhost:3000/register/";
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

    public SignupPage fillPassword(String value){
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
        return new CalendarPage(this.driver);
    }

    public SignupPage clickCreateButtonExpectingFailure(){
        clickButton(this.createButton);
        return this;
    }
    public String getAlertMessage(){
        return findOne(this.alert).getText();
    }

    public CalendarPage signupAsDefaultUser(){
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

    public SignupPage signupWithUserExpectingFailure(User user){
        fillCompanyName(userService.getCompanyForUser(user).getName());
        fillEmail(user.getEmail());
        fillFirstName(user.getName());
        fillLastName(user.getLastName());
        fillPassword(user.getRawPassword());
        fillPasswordConfirmation(user.getRawPassword());
        return clickCreateButtonExpectingFailure();
    }


    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
