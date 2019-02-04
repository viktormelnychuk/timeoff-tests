package com.viktor.timeofftests.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupPage extends BasePage{
    private WebDriver driver;
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
    @Override
    public void open(String url){
        super.open(url);
    }

    public void fillCompanyName(String value){
        fillInputField(this.companyName, value);
    }

    public void fillFirstName(String value){
        fillInputField(this.firstName, value);
    }

    public void fillLastName(String value){
        fillInputField(this.lastName, value);
    }

    public void fillEmail(String value){
        fillInputField(this.email, value);
    }

    public void fillPassword(String value){
        fillInputField(this.password, value);
    }

    public void fillPasswordConfirmation(String value){
        fillInputField(this.confirmPassword, value);
    }

    public void selectCountry(String value) throws Exception {
        selectOption(this.country, value);
    }

    public void  selectTimeZone(String value) throws Exception {
        selectOption(this.timezone, value);
    }

    public void  clickCreateButtonExpectingSuccess(){
        clickButton(this.createButton);
    }

    public String getAlertMessage(){
        return findOne(this.alert).getText();
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
