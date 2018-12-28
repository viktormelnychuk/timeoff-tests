package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class AddNewLeaveTypeModal extends BasePage {
    private WebDriver driver;

    private By nameInput = By.id("leave_type_name_new");
    private By userAllowanceCheckbox = By.id("leave_type_use_allovance_new");
    private By limitInput = By.id("leave_type_limit_new");
    private By createButton = By.xpath("//form[@id='leave_type_new_form']//button[@type='submit']");
    private By colorPicker = By.xpath("//form[@id='leave_type_new_form']//button[contains(@class,'leave_type_color')]");
    public AddNewLeaveTypeModal (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public AddNewLeaveTypeModal setName(String name){
        fillInputField(nameInput, name);
        return this;
    }

    public AddNewLeaveTypeModal setAllowance(boolean b){
        if(Objects.equals(findOne(userAllowanceCheckbox).getAttribute("checked"), "true") != b){
            clickButton(userAllowanceCheckbox);
        }
        return this;
    }

    public AddNewLeaveTypeModal setLimit(int limit){
        fillInputField(limitInput, Integer.toString(limit));
        return this;
    }

    public void setColor (String color){
        clickButton(colorPicker);
        findOne(By.linkText(color)).click();
    }

    public void setLimit(String limit) {
        setLimit(Integer.parseInt(limit));
    }

    public GeneralSettingsPage clickCreateButton(){
        clickButton(createButton);
        return new GeneralSettingsPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

}
