package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeePage extends EmployeeBasePage {

    private By saveButton = By.id("save_changes_btn");
    private By deleteButton = By.id("remove_btn");
    @Override
    public String getBaseUrl() {
        return null;
    }

    public EmployeePage(WebDriver driver){
        super(driver);
    }

    public void clickSaveChangesButton() {
        clickButton(saveButton);
    }

    public void clickDeleteButton() {
        clickButton(deleteButton);
    }
}
