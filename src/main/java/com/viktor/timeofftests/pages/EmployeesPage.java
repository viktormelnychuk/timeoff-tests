package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.forms.EmployeeRow;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeesPage extends BasePage {

    private WebDriver driver;

    private By employeesTable = By.xpath("//table[@class='table table-hover']//tbody/tr");
    private String departmentNameFilterQuery = "//div[@class='col-md-3 list-group all-departments']//a[text()='%s']";

    @Override
    public String getBaseUrl() {
        return null;
    }

    public EmployeesPage (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public List<EmployeeRow> getAllDisplayedEmployees(){
        List<EmployeeRow> result = new ArrayList<>();
        try {
            List<WebElement> table = findAllBy(employeesTable);
            for (WebElement row : table) {
                EmployeeRow e = new EmployeeRow();
                e.setName(row.findElement(By.xpath(".//td[@class='user-link-cell']/a")).getText());
                String depLink = row.findElement(By.xpath(".//td[@class='user_department']//a")).getAttribute("href");
                String depId = depLink.split("/departments/edit/")[1].split("/")[0];
                e.setDepartmentId(Integer.parseInt(depId));

                String admin = row.findElement(By.xpath(".//td[3]")).getText();
                e.setAdmin(getBoolFromYesNo(admin));

                String availableAllowance = row.findElement(By.xpath(".//td[@class='vpp-days-remaining']")).getText();
                e.setAvailableAllowance(Integer.parseInt(availableAllowance));

                String daysUsed = row.findElement(By.xpath(".//td[@class='vpp-days-used']")).getText();
                e.setDaysUsed(Integer.parseInt(daysUsed));
                result.add(e);
            }
        return result;
        } catch (TimeoutException e){
            /*
            fail silently because no employees are displayed
            This is ok because list validated later
             */
            return Collections.emptyList();
        }
    }

    public void filterByDepartmentName(String departmentName) {
        By locator = By.xpath(String.format(departmentNameFilterQuery, departmentName));
        clickButton(locator);
    }
}

