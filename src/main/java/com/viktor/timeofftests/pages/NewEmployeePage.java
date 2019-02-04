package com.viktor.timeofftests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NewEmployeePage extends EmployeeBasePage {


    private By addNewUserButton = By.id("add_new_user_btn");

    public NewEmployeePage (WebDriver driver){
        super(driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }


    public void clickCreateButton() {
        clickButton(addNewUserButton);
    }
}
