package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class NewAbsenceModal extends BasePage {
    private WebDriver driver;

    private By leaveTypeSelect = By.xpath("//select[@id='leave_type'][@name='leave_type']");

    public NewAbsenceModal(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<String> getDisplayedLeaveTypesAsString(){
        List<String> result = new ArrayList<>();
        Select leaveTypesSelect = new Select(findOne(leaveTypeSelect));
        leaveTypesSelect.getOptions().forEach((element)->{
            result.add(element.getText());
        });
        return result;
    }
}
