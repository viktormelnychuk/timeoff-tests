package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.pages.SignupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class SignupSteps extends BaseStep {
    private static SignupSteps instance;
    private final String pageUrl = "/signup";
    private SignupPage currentPage;

    public static SignupSteps getInstance(){
        if(instance == null){
            instance = new SignupSteps();
            return instance;
        } else {
            return instance;
        }
    }
    @Step("Open signup page")
    public void openSignupPage(){
        WebDriver driver = DriverUtil.getDriver(DriverEnum.CHROME);
        String url = getBaseURL()+ pageUrl;
        driver.get(url);
        currentPage = new SignupPage(driver);
    }

    @Step("Filling email field with {email}")
    public void enterEmail(String email){
        currentPage.fillEmail(email);
    }

    @Step("Enter company name: {name}")
    public void enterCompanyName(String name){
        currentPage.fillCompanyName(name);
    }
    @Step("Enter name: {firstName}")
    public void enterFirstName(String firstName){
        currentPage.fillFirstName(firstName);
    }

    @Step("Enter last name {lastName}")
    public void enterLastName(String lastName){
        currentPage.fillLastName(lastName);
    }

    @Step("Enter password: {password}")
    public void enterPassword(String password){
        currentPage.fillPassword(password);
    }

    @Step("Enter password confirmation:{passwordConfirmation}")
    public void enterPasswordConfirmation(String passwordConfirmation){
        currentPage.fillPasswordConfirmation(passwordConfirmation);
    }

    @Step("Select country: {country}")
    public void selectCountry(String country){
        currentPage.selectCountry(country);
    }

    @Step("Select timezone: {timeZone}")
    public void selectTimeZone(String timeZone){
        currentPage.selectTimeZone(timeZone);
    }

    @Step("Click submit button")
    public void submitExpectingSuccess(){
        currentPage.clickCreateButtonExpectingSuccess();
    }
}
