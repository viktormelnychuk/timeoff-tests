package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SignupPage {
    private WebDriver driver;
    private WebElement companyName;
    private WebElement firstName;
    private WebElement lastName;
    private WebElement email;
    private WebElement password;
    private WebElement confirmPassword;
    private Select country;
    private Select timezone;
    private WebElement createButton;
    public SignupPage(WebDriver driver) {
        this.driver = driver;
        if (!this.driver.getCurrentUrl().equalsIgnoreCase("http://localhost:3000/register/")){
            this.driver.get("http://localhost:3000/register/");
        }
        this.companyName = this.driver.findElement(By.id("company_name_inp"));
        this.firstName = this.driver.findElement(By.id("name_inp"));
        this.lastName = this.driver.findElement(By.id("lastname_inp"));
        this.email = this.driver.findElement(By.id("email_inp"));
        this.password = this.driver.findElement(By.id("pass_inp"));
        this.confirmPassword = this.driver.findElement(By.id("confirm_pass_inp"));
        this.country = new Select(this.driver.findElement(By.id("country_inp")));
        this.timezone = new Select(this.driver.findElement(By.id("timezone_inp")));
        this.createButton = this.driver.findElement(By.id("submit_registration"));
    }
    public SignupPage fillCompanyName(String value){
        fillInputFeld(this.companyName, value);
        return this;
    }

    public SignupPage fillFirstName(String value){
        fillInputFeld(this.firstName, value);
        return this;
    }

    public SignupPage fillLastName(String value){
        fillInputFeld(this.lastName, value);
        return this;
    }

    public SignupPage fillEmail(String value){
        fillInputFeld(this.email, value);
        return this;
    }

    public SignupPage fillPassword (String value){
        fillInputFeld(this.password, value);
        return this;
    }

    public SignupPage fillPasswordConfirmation(String value){
        fillInputFeld(this.confirmPassword, value);
        return this;
    }

    public SignupPage selectCountry(String value){
        this.country.selectByVisibleText("GB: United Kingdom");
        return this;
    }

    public SignupPage selectTimeZone(String value){
        this.timezone.selectByVisibleText("Europe/London");
        return this;
    }

    public CalendarPage clickCreateButton(){
        this.createButton.click();
        return new CalendarPage(this.driver);
    }

    private void fillInputFeld(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
    }

}
