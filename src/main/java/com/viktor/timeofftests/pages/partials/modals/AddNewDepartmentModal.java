package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AddNewDepartmentModal extends BasePage {
    private WebDriver driver;
    private By nameInput = By.id("department_name_new");
    private By allowanceSelect = By.xpath("//select[@name='allowance__new']");
    private By includePublicChk = By.id("department_include_public_holiday_new");
    private By accruedAllowanceChk = By.id("department_is_accrued_allowance_new");
    private By bossSelect = By.xpath("//select[@name='boss_id__new']");
    private By createButton = By.xpath("//form[@id='add_new_department_form']//button[@type='submit']");
    private By form = By.id("add_new_department_form");
    public AddNewDepartmentModal(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public void fillName(String name){
        fillInputField(nameInput, name);
    }

    public void selectAllowance(String allowance) throws Exception {
        selectOption(allowanceSelect, allowance);
    }

    public void setIncludePublicHolidays(boolean include){
        WebElement element = findOne(includePublicChk);
        if(element.isSelected() != include){
            element.click();
        }
    }

    public void setAccruedAllowance (boolean include){
        WebElement element = findOne(accruedAllowanceChk);
        if(element.isSelected() != include){
            element.click();
        }
    }

    public AddNewDepartmentModal setBoss (int userId) throws Exception {
        selectOption(bossSelect, String.valueOf(userId));
        return this;
    }

    public boolean publicHolidaysChecked(){
        return findOne(includePublicChk).isSelected();
    }

    public boolean accruedChecked(){
        return findOne(accruedAllowanceChk).isSelected();
    }


    public void clickCreateButtonExpectingSuccess(){
        clickButton(createButton);
    }

    public boolean modalDisplayed(){
        return findOne(form).isDisplayed();
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
