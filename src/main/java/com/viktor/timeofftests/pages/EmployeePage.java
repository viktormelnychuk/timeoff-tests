package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeePage extends EmployeeBasePage {
    private WebDriver driver;

    private By saveButton = By.id("save_changes_btn");
    @Override
    public String getBaseUrl() {
        return null;
    }

    public EmployeePage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public void clickSaveChangesButton() {
        clickButton(saveButton);
    }
}
