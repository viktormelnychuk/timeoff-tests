package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.partials.MenuBar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CalendarPage extends BasePage {
    private WebDriver driver;

    @FindBy(xpath = "//div[@role='alert']")
    private WebElement message;

    @FindBy(xpath = "//div[@class='row']/div")
    private WebElement employeeName;

    public String getBaseUrl() {
        return "http://localhost:3000/calendar/";
    }

    public CalendarPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    public String getAlertMessage(){
        return this.message.getText();
    }
    public String getEmployeeGreeting(){
        return this.employeeName.getText();
    }

    @Override
    public WebDriver getDriver(){
        return this.driver;
    }
}
