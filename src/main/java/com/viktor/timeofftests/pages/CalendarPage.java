package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarPage {
    private WebDriver driver;
    private WebElement message;
    private WebElement employeeName;
    public CalendarPage(WebDriver driver) {
        this.driver = driver;
        this.message = this.driver.findElement(By.xpath("//div[@role='alert']"));
        this.employeeName = this.driver.findElement(By.xpath("//div[@class='row']/div"));
    }
    public String getAlertMessage(){
        return this.message.getText();
    }
    public String getEmployeeGreeting(){
        return this.employeeName.getText();
    }
}
