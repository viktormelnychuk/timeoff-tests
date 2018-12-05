package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.services.LeaveTypeService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class LeaveTypesSettings extends BasePage {

    private WebDriver driver;
    private By leaveTypeEditForm = By.xpath("//form[@id='leave_type_edit_form']");
    private String leaveTypeNameQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']";
    String leaveTypeOrderQuery = "//form[@id='leave_type_edit_form']//input[@value='Sick Leave']/../span/input";
    private By saveChangesButton = By.xpath("//form[@id='leave_type_edit_form']//button[@type='submit']");
    public LeaveTypesSettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<LeaveType> getDisplayedLeaveTypes(){
        List<WebElement> rows = findAllBy(By.xpath("//form[@id='leave_type_edit_form']/div[contains(.,'allowance')]"));
        return LeaveTypeService.getInstance().deserializeLeaveTypes(rows);
    }

    public List<String> getDisplayedLeaveTypesAsString(){
        List<LeaveType> rows = getDisplayedLeaveTypes();
        List<String> result = new ArrayList<>();
        rows.forEach((item)->{
            result.add(item.getName());
        });
        return result;
    }

    public LeaveTypesSettings editLeaveTypeName(String oldName, String newName){
        By locator = By.xpath(String.format(leaveTypeNameQuery,oldName));
        fillInputField(locator, newName);
        return this;
    }

    public GeneralSettingsPage clickSaveButton(){
        clickButton(saveChangesButton);
        return new GeneralSettingsPage(this.driver);
    }

    public LeaveTypesSettings setPrimaryLeaveType(String name){
        By locator = By.xpath(String.format(leaveTypeOrderQuery, name));
        clickButton(locator);
        return this;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
