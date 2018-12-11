package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.partials.modals.AddNewDepartmentModal;
import com.viktor.timeofftests.services.DepartmentService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentsPage extends BasePage {
    private WebDriver driver;
    private DepartmentService departmentService = DepartmentService.getInstance();
    private By departmentsTable = By.xpath("//table[@class='table table-hover']//tbody/tr");
    private By addNewDepartmentButton = By.id("add_new_department_btn");
    public DepartmentsPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public DepartmentsPage navigate(){
        LoginPage loginPage  = new LoginPage(driver);
        loginPage.open();
        loginPage.loginWithDefaultUser();
        driver.get(getBaseUrl());
        return new DepartmentsPage(driver);
    }

    public List<Department> getDisplayedDepartments(){
        List<WebElement> table = findAllBy(departmentsTable);
        return departmentService.deserializeDepartments(table);
    }
    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/";
    }

    public DepartmentsPage reload() {
        this.driver.navigate().refresh();
        return this;
    }

    public String getDisplayedManagerNameForDepartment(Department defaultDep) {
        By locator = By.xpath(String.format("//table//tbody//a[text()='%s']/../..//td[2]", defaultDep.getName()));
        return findOne(locator).getText();
    }

    public Map<String, Integer> getDisplayedEmployeesNumber() {
        String numberOfUsersQuery = "//table//tbody//a[text()='%s']/../..//td[4]";
        Map<String, Integer> result = new HashMap<>();
        List<Department> displayedDepartments = getDisplayedDepartments();
        for (Department department : displayedDepartments) {
            By locator = By.xpath(String.format(numberOfUsersQuery, department.getName()));
            Integer numberOfEmployeesDisplayed = Integer.parseInt(findOne(locator).getText());
            result.put(department.getName(), numberOfEmployeesDisplayed);
        }
        return result;
    }

    public AddNewDepartmentModal clickAddNewDepartmentButton(){
        clickButton(addNewDepartmentButton);
        return new AddNewDepartmentModal(this.driver);
    }
}
