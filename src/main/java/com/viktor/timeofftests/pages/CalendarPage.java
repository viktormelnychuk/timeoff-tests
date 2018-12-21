package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CalendarPage extends BasePage {
    private WebDriver driver;

    private By message = By.xpath("//div[@role='alert']");
    private By employeeName = By.xpath("//div[@class='row']/div");

    public String getBaseUrl() {
        return "http://localhost:3000/calendar/";
    }

    public CalendarPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    public String getAlertMessage(){
        return findOne(this.message).getText();
    }
    public String getEmployeeGreeting(){
        return findOne(this.employeeName).getText();
    }

    @Override
    public WebDriver getDriver(){
        return this.driver;
    }

    public String getTitle() {
        return findOne(By.cssSelector("h1")).getText();
    }
}
