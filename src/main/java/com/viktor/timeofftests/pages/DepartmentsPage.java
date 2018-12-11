package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.services.DepartmentService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DepartmentsPage extends BasePage {
    private WebDriver driver;
    private By departmentsTable = By.xpath("//table[@class='table table-hover']//tbody/tr");
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
        return DepartmentService.getInstance().deserializeDepartments(table);
    }
    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/departments/";
    }

    public DepartmentsPage reload() {
        this.driver.navigate().refresh();
        return this;
    }
}
