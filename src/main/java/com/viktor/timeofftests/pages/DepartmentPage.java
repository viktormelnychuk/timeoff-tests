package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DepartmentPage extends BasePage {

    private final WebDriver driver;
    private By nameInp = By.id("name");
    private By managerSelect = By.id("manager_id");
    private By allowanceSelect = By.id("allowance_select");
    private By usePublicHolidayCheck = By.id("use_bank_holidays_inp");
    private By accruedCheck = By.id("is_accrued_allowance_inp");
    private By saveChangesButton = By.id("save_changes_btn");
    private By alert = By.xpath("//div[@role='alert']");
    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/edit/%s/";
    }

    public DepartmentPage fillName(String value){
        fillInputField(nameInp, value);
        return this;
    }

    public DepartmentPage selectManger(int userId){
        selectOption(managerSelect, String.valueOf(userId));
        return this;
    }

    public DepartmentPage setIncludePublicHolidays(boolean value){
        WebElement element = findOne(usePublicHolidayCheck);
        if(element.isSelected()!=value){
            element.click();
        }
        return this;
    }

    public DepartmentPage setAccruedAllowance(boolean value){
        WebElement element = findOne(accruedCheck);
        if(element.isSelected()!=value){
            element.click();
        }
        return this;
    }

    public DepartmentPage setAllowance(int allowance){
        selectOption(allowanceSelect, String.valueOf(allowance));
        return this;
    }

    public DepartmentPage clickSaveButton(){
        clickButton(saveChangesButton);
        return new DepartmentPage(this.driver);
    }

    public DepartmentPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public String getAlert() {
        return findOne(alert).getText();
    }
}
