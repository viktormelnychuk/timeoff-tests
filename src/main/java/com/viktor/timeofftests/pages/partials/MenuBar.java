package com.viktor.timeofftests.pages.partials;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.pages.LoginPage;
import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class MenuBar extends BasePage {

    private final WebDriver driver;
    private By meMenu = By.id("me_menu");
    private By logOutLink = By.linkText("Logout");
    private By employeesButton = By.linkText("Employees");
    private By newAbsenceButton = By.id("book_time_off_btn");
    private By gearButton = By.xpath("//a[@href='#'][@role='button']");
    private By departmentsButton = By.linkText("Departments");
    public String getBaseUrl() {
        return null;
    }

    public MenuBar(WebDriver driver) {
        this.driver = driver;
        super.setDriver(driver);
    }

    public boolean employeesButtonDisplayed(){
        try {
            this.driver.findElement(employeesButton);
            return true;
        } catch (NoSuchElementException ex){
            return false;
        }
    }

    public NewAbsenceModal openNewAbsenceModal(){
        clickButton(newAbsenceButton);
        return new NewAbsenceModal(this.driver);
    }

    public DepartmentsPage navigateToDepartments(){
        clickButton(gearButton);
        clickButton(departmentsButton);
        return new DepartmentsPage(this.driver);
    }

    public LoginPage logout(){
        clickButton(this.meMenu);
        clickButton(this.logOutLink);
        return new LoginPage(this.driver);
    }

}
