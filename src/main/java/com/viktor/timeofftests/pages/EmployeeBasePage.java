package com.viktor.timeofftests.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EmployeeBasePage extends BasePage {
    private By nameInput = By.id("name_inp");
    private By lastNameInput = By.id("lastname_inp");
    private By emailInput = By.id("email_inp");
    private By departmentSelect = By.xpath("//select[@id='select_inp'][@name='department']");
    private By adminCheckbox = By.id("admin_inp");
    private By autoApproveCheckbox = By.id("auto_approve_inp");
    private By startedOnDatePicker = By.id("start_date_inp");
    private By endedOnDatePicker = By.id("end_date_inp");
    private By passwordInput = By.id("password_inp");
    private By confirmPasswordInput = By.id("confirm_password_inp");

    public EmployeeBasePage (WebDriver driver){
        super(driver);
    }
    public void fillFirstName (String v){
        if(StringUtils.isEmpty(v)){
            return;
        }
        fillInputField(nameInput, v);
    }

    public void fillLastName (String v){
        if(StringUtils.isEmpty(v)){
            return;
        }
        fillInputField(lastNameInput, v);
    }

    public void fillEmailField(String v){
        if(StringUtils.isEmpty(v)){
            return;
        }
        fillInputField(emailInput, v);
    }

    public void selectDepartment (String v) throws Exception {
        if(StringUtils.isEmpty(v)){
            return;
        }
        selectOption(departmentSelect, v);
    }
    public void setAdmin (boolean b){
        setCheckBox(adminCheckbox, b);
    }

    public void setAutoApprove (boolean b){
        setCheckBox(autoApproveCheckbox, b);
    }

    public void fillStartedOn(LocalDate date){
        if(date==null){
            return;
        }
        WebElement inp = findOne(startedOnDatePicker);
        String dateFormat = inp.getAttribute("data-date-format").replaceAll("mm","MM");
        fillInputField(startedOnDatePicker,date.format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    public void fillEndedOn(LocalDate date){
        WebElement inp = findOne(endedOnDatePicker);
        String dateFormat = inp.getAttribute("data-date-format").replaceAll("mm","MM");
        if(Objects.isNull(date)){
            // ended on is not provided so should not be filled
            return;
        } else {
            fillInputField(endedOnDatePicker, date.format(DateTimeFormatter.ofPattern(dateFormat)));
        }

    }

    public void fillPassword (String v){
        if(StringUtils.isEmpty(v)){
            return;
        }
        fillInputField(passwordInput, v);
    }

    public void fillPasswordConfirmation (String v){
        if(StringUtils.isEmpty(v)){
            return;
        }
        fillInputField(confirmPasswordInput, v);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
