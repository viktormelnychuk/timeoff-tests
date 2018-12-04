package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.services.LeaveTypeService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LeaveTypesSettings extends BasePage {

    private WebDriver driver;
    private By leaveTypeEditForm = By.xpath("//form[@id='leave_type_edit_form']");
    public LeaveTypesSettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<LeaveType> getDisplayedLeaveTypes(){
        List<WebElement> rows = findOne(leaveTypeEditForm).findElements(By.className("row"));
        return LeaveTypeService.getInstance().deserializeLeaveTypes(rows);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
