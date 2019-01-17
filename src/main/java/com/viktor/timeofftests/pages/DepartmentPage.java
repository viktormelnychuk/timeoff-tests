package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.pages.partials.modals.AddSupervisorsModal;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepartmentPage extends BasePage {

    private final WebDriver driver;
    private By nameInp = By.id("name");
    private By managerSelect = By.id("manager_id");
    private By allowanceSelect = By.id("allowance_select");
    private By usePublicHolidayCheck = By.id("use_bank_holidays_inp");
    private By accruedCheck = By.id("is_accrued_allowance_inp");
    private By saveChangesButton = By.id("save_changes_btn");
    private By alert = By.xpath("//div[@role='alert']");
    private By addNewSecondarySupervisorLink = By.xpath("//form[@id='department_edit_form']//a[@data-vpp-add-new-secondary-supervisor='1']");
    private By allSecondarySupervisors = By.xpath("//form[@id='department_edit_form']//ul//li[descendant::button]");
    private String deleteSecondarySupervisorQuery = "//button[@name='remove_supervisor_id' and @value='%s']";

    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/edit/%s/";
    }

    public void fillName(String value){
        fillInputField(nameInp, value);
    }

    public void selectManger(int userId){
        selectOption(managerSelect, String.valueOf(userId));
    }

    public void setIncludePublicHolidays(boolean value){
        WebElement element = findOne(usePublicHolidayCheck);
        if(element.isSelected()!=value){
            element.click();
        }
    }

    public void setAccruedAllowance(boolean value){
        WebElement element = findOne(accruedCheck);
        if(element.isSelected()!=value){
            element.click();
        }
    }

    public void deleteSecondarySupervisor(int amount){
        List<Integer> secondaryApproversIds = getSecondaryApproversIds();
        List<Integer> toDelete = secondaryApproversIds.subList(0, amount);
        for (Integer userId : toDelete) {
            By locator = By.xpath(String.format(deleteSecondarySupervisorQuery, userId));
            clickButton(locator);
        }
    }

    public void setAllowance(int allowance){
        selectOption(allowanceSelect, String.valueOf(allowance));
    }

    public void clickSaveButton(){
        clickButton(saveChangesButton);
    }

    public DepartmentPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public AddSupervisorsModal clickAddSecondarySupervisors(){
        clickButton(addNewSecondarySupervisorLink);
        return new AddSupervisorsModal(this.driver);
    }

    public String getAlert() {
        return findOne(alert).getText();
    }

    public List<Integer> getSecondaryApproversIds() {
        List<Integer> result = new ArrayList<>();
        try {
            List<WebElement> elements = findAllBy(allSecondarySupervisors);
            for (WebElement element : elements) {
                result.add(Integer.parseInt(element.findElement(By.xpath(".//button")).getAttribute("value")));
            }
            return result;
        } catch (TimeoutException e){
            return Collections.emptyList();
        }
    }

    public String getDepartmentName(){
        return findOne(nameInp).getAttribute("value");
    }

    public int getManagerId(){
        Select manger = new Select(findOne(managerSelect));
        return Integer.parseInt(manger.getFirstSelectedOption().getAttribute("value"));
    }

    public double getAllowance(){
        Select allowance = new Select(findOne(allowanceSelect));
        return Double.parseDouble(allowance.getFirstSelectedOption().getAttribute("value"));
    }

    public boolean isPublicHolidaysIncluded(){
        return findOne(usePublicHolidayCheck).isSelected();
    }

    public boolean isAccruedAllowance(){
        return findOne(accruedCheck).isSelected();
    }
}
