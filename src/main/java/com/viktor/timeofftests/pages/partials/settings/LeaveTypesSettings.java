package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
import com.viktor.timeofftests.services.LeaveTypeService;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaveTypesSettings extends BasePage {

    private WebDriver driver;
    private By leaveTypeEditForm = By.xpath("//form[@id='leave_type_edit_form']");
    private String leaveTypeNameQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']";
    private String leaveTypeOrderQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']/../span/input";
    private String leaveTypeUseAllowanceQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']/../../../div/input[@type='checkbox']";
    private String leaveTypeLimitQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']/../../../div/input[contains(@name,'limit')]";
    private String leaveTypeDeleteQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']/../../../div/button[contains(@data-tom-leave-type-order,'remove')]";
    private String leaveTypeColorQuery = "//form[@id='leave_type_edit_form']//input[@value='%s']/../../..//button[contains(@class,'leave_type_color')]";
    private By saveChangesButton = By.xpath("//form[@id='leave_type_edit_form']//button[@type='submit']");
    private By addNewButton = By.id("add_new_leave_type_btn");
    public LeaveTypesSettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<LeaveType> getDisplayedLeaveTypes(int companyId){
        try {
            List<WebElement> rows = findAllBy(By.xpath("//form[@id='leave_type_edit_form']/div[contains(.,'allowance')]"));
            return deserializeLeaveTypes(rows, companyId);
        } catch (TimeoutException e){
            /*
                If cannot find elements - return empty arrayList.
                If elements aren't displayed due to a bug - it will fail on assertion in the test
             */
            return new ArrayList<>();
        }

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

    public LeaveTypesSettings setUseAllowanceForLeave(String name, boolean b) {
        By locator = By.xpath(String.format(leaveTypeUseAllowanceQuery,name));
        if(Objects.equals(findOne(locator).getAttribute("checked"), "true") != b){
            clickButton(locator);
        }
        return this;
    }

    public LeaveTypesSettings setLimit(String name, int limit) {
        By locator = By.xpath(String.format(leaveTypeLimitQuery,name));
        fillInputField(locator, Integer.toString(limit));
        return this;
    }


    public void setColor(String toEdit, String color) {
        By locator = By.xpath(String.format(leaveTypeColorQuery, toEdit));
        clickButton(locator);
        findOne(By.linkText(color)).click();
    }

    public GeneralSettingsPage deleteLeave (String name){
        By locator = By.xpath(String.format(leaveTypeDeleteQuery,name));
        clickButton(locator);
        return new GeneralSettingsPage(this.driver);
    }

    public AddNewLeaveTypeModal clickAddButton(){
        clickButton(addNewButton);
        return new AddNewLeaveTypeModal(this.driver);
    }
    public List<LeaveType> deserializeLeaveTypes(List<WebElement> rows, int companyId){
        List<LeaveType> leaveTypes = new ArrayList<>();
        for (WebElement row : rows) {
            LeaveType leaveType = new LeaveType();
            String name = row.findElement(By.xpath(".//input[contains(@data-tom-leave-type-order,'name')]")).getAttribute("value");
            String color = row.findElement(By.xpath(".//div[contains(@data-tom-leave-type-order,'colour_')]/input[@type='hidden']")).getAttribute("value");
            boolean useAllowance = row.findElement(By.xpath(".//input[contains(@data-tom-leave-type-order,'allowance_')]")).isSelected();
            String limit = row.findElement(By.xpath(".//input[contains(@data-tom-leave-type-order,'limit_')]")).getAttribute("value");
            String nameWithId = row.findElement(By.xpath(".//input[contains(@data-tom-leave-type-order,'name')]")).getAttribute("name");
            String id = nameWithId.split("__")[1];
            String xpathForChecked = String.format(".//input[@value='%s']", id);
            int sortOrder = getSortOrder(row.findElement(By.xpath(xpathForChecked)));
            leaveType.setColorHex(color);
            leaveType.setName(name);
            leaveType.setUseAllowance(useAllowance);
            leaveType.setLimit(Integer.parseInt(limit));
            leaveType.setId(Integer.parseInt(id));
            leaveType.setSortOrder(sortOrder);
            leaveType.setCompanyId(companyId);
            leaveTypes.add(leaveType);
        }
        return leaveTypes;
    }

    private int getSortOrder(WebElement element){
        if(element.isSelected()){
            return 1;
        } else {
            return 0;
        }
    }
}
