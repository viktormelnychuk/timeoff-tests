package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public class CalendarPage extends BasePage {
    private WebDriver driver;

    private By employeeName = By.xpath("//div[@class='row']/div");
    private By nominalAllowance = By.xpath("//dd//em[text()='Nominal allowance ']/../span");
    private By carriedOver = By.xpath("//dd//em[text()='Carried over from 2018']/../span");
    private By individualAdjustments = By.xpath("//dd//em[text()='Individual adjustment']/../span");
    private By startEndOfEmployment = By.xpath("//dd//em[text()='Start/end of employment']/../span");
    private By usedSoFar = By.xpath("//dd//em[text()='Used so far']/../span");
    private By availableAllowance = By.xpath("//dt[@data-tom-days-available-in-allowance]");
    private By daysAvailableText = By.xpath("//span[@data-tom-total-days-in-allowance]");
    private By supervisor = By.xpath("//em[text()=' Supervisor: ']//../span//a");
    private By department = By.xpath("//em[text()='Department:']//../span//a");
    private String allowanceInYearQuery = "//em[text()='Allowance in %s:']//../span";
    public String getBaseUrl() {
        return "http://localhost:3000/calendar/";
    }

    public CalendarPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    public String getEmployeeGreeting(){
        return findOne(this.employeeName).getText();
    }
    public int getNominalAllowance (){
        return Integer.parseInt(findOne(nominalAllowance).getText());
    }
    public int getCarriedOver(){
        return Integer.parseInt(findOne(carriedOver).getText());
    }

    public int getIndividualAdjustments (){
        return Integer.parseInt(findOne(individualAdjustments).getText());
    }

    public int getStartEndOfYear(){
        return Integer.parseInt(findOne(startEndOfEmployment).getText());
    }

    public int getUsedSoFar(){
        return Integer.parseInt(findOne(usedSoFar).getText());
    }

    public int getAvailableAllowance(){
        return Integer.parseInt(findOne(availableAllowance).getText());
    }

    public int getDaysAvailable(){
        return Integer.parseInt(findOne(daysAvailableText).getText());
    }

    public String getSupervisorName(){
        return findOne(supervisor).getText();
    }

    public String getSupervisorEmail(){
        String mailtoLink = findOne(supervisor).getAttribute("href");
        String[] split = mailtoLink.split(":");
        return split[1];
    }

    public String getDepartmentName(){
        return findOne(department).getText();
    }

    public String getDepartmentAllowanceInYear(){
        By locator = By.xpath(String.format(allowanceInYearQuery, LocalDate.now().getYear()));
        return findOne(locator).getText();
    }

    @Override
    public WebDriver getDriver(){
        return this.driver;
    }

    public String getTitle() {
        return findOne(By.cssSelector("h1")).getText();
    }
}
