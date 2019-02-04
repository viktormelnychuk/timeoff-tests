package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.partials.modals.AddNewDepartmentModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class DepartmentsPage extends BasePage {
    private WebDriver driver;
    private By departmentsTable = By.xpath("//table[@class='table table-hover']//tbody/tr");
    private By addNewDepartmentButton = By.id("add_new_department_btn");
    public DepartmentsPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public DepartmentsPage navigate(){
        if(DriverUtil.sessionCookiePresent(this.driver)){
           return menuBar.navigateToDepartments();
        }
        else return null;
    }
    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/";
    }

    public DepartmentsPage reload() {
        this.driver.navigate().refresh();
        return this;
    }

    public String getDisplayedManagerNameForDepartment(String departmentName) {
        By locator = By.xpath(String.format("//table//tbody//a[text()='%s']/../..//td[2]", departmentName));
        return findOne(locator).getText();
    }

    public List<Department> getDisplayedDepartments(){
        return deserializeDepartments(0);
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

    public void clickDepartmentLink(String name) {
        clickButton(By.linkText(name));
    }
    public List<Department> deserializeDepartments(int companyId) {
        List<Department> result = new ArrayList<>();
        List<WebElement> table = findAllBy(departmentsTable);
        for (WebElement row:table) {
            Department department = new Department();
            String depLink = row.findElement(By.xpath(".//td[1]/a")).getAttribute("href");
            String depId = depLink.split("/departments/edit/")[1].split("/")[0];
            department.setId(Integer.parseInt(depId));

            String bossLink = row.findElement(By.xpath(".//td[2]/a")).getAttribute("href");
            try {
                String bossId = bossLink.split("/users/edit/")[1].split("/")[0];
                department.setBossId(Integer.parseInt(bossId));
            } catch (IndexOutOfBoundsException e){
                department.setBossId(0);
            }

            department.setName(row.findElement(By.xpath(".//td[1]")).getText());
            String allowanceString = row.findElement(By.xpath(".//td[3]")).getText();
            int allowance;
            if(allowanceString.equals("None")){
                allowance = 0;
            } else {
                allowance = Integer.parseInt(allowanceString);
            }
            department.setAllowance(allowance);

            String publicHolidays = row.findElement(By.xpath(".//td[5]")).getText();
            department.setIncludePublicHolidays(getBoolFromYesNo(publicHolidays));

            String accruedAllowance = row.findElement(By.xpath(".//td[6]")).getText();
            department.setAccuredAllowance(getBoolFromYesNo(accruedAllowance));
            department.setCompanyId(companyId);
            result.add(department);
        }
        return result;
    }

    public Map<String, String> getDisplayedManagers() {
        Map<String, String> result = new HashMap<>();
        List<Department> allDeps = getDisplayedDepartments();
        for (Department department : allDeps) {
            result.put(department.getName(), getDisplayedManagerNameForDepartment(department.getName()));
        }
        return result;
    }
}
