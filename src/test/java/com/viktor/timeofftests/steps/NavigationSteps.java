package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import lombok.extern.log4j.Log4j2;

import javax.xml.soap.Text;
import java.util.Calendar;
import java.util.Objects;

import static org.junit.Assert.*;
@Log4j2
public class NavigationSteps {

    private World world;

    public NavigationSteps (World world){
        this.world = world;
    }

    @Given("I am on {string} page")
    public void iAmOnPage(String page) throws Exception {
        log.info("Navigating to [{}] page", page);
        switch (page.toLowerCase()){
            case Pages.CALENDAR:
                world.driver.get(TextConstants.CalendarPageConstants.PAGE_URL);
                break;
            case Pages.LOGIN:
                world.driver.get(TextConstants.LoginPageConstants.PAGE_URL);
                break;
            case Pages.REGISTER:
                world.driver.get(TextConstants.RegisterPageConstants.PAGE_URL);
                break;
            case Pages.SETTINGS:
                navigateToGeneralSettings();
                break;
            case Pages.DEPARTMENTS:
                navigateToDepartmentsPage();
                break;
            case Pages.EMPLOYEES:
                navigateToEmployeesPage();
                break;
            default:
                throw new Exception("Page was not found");
        }
    }

    @Then("I should be on {string} page")
    public void iShouldBeOnPage(String page) throws Exception {
        log.info("Verifying user is on [{}] page", page);
        switch (page.toLowerCase()){
            case Pages.CALENDAR:
                assertTrue(world.driver.getCurrentUrl().contains(TextConstants.CalendarPageConstants.PAGE_URL));
                break;
            case Pages.LOGIN:
                assertTrue(world.driver.getCurrentUrl().contains(TextConstants.LoginPageConstants.PAGE_URL));
                break;
            case Pages.REGISTER:
                assertTrue(world.driver.getCurrentUrl().contains(TextConstants.RegisterPageConstants.PAGE_URL));
                break;
            default:
                throw new Exception("Page was not found");
        }
    }
    @Given("I am on {string} department page")
    public void iAmOnDepartmentPage(String departmentName) {
        navigateToDepartmentsPage();
        DepartmentsPage page = new DepartmentsPage(world.driver);
        page.clickDepartmentLink(departmentName);
    }
    public void navigateToGeneralSettings() {
        String currentUrl = world.driver.getCurrentUrl();
        if(!Objects.equals(currentUrl, TextConstants.GeneralSettingsConstants.PAGE_URL)){
            CalendarPage calendarPage = login();
            calendarPage.menuBar.navigateToGeneralSettings();
        }
    }
    public void navigateToDepartmentsPage(){
        world.driver.get(TextConstants.DepartmentsConstants.PAGE_URL);
        String currentUrl = world.driver.getCurrentUrl();
        if(!Objects.equals(currentUrl, TextConstants.DepartmentsConstants.PAGE_URL)){
            CalendarPage calendarPage = login();
            calendarPage.menuBar.navigateToDepartments();
        }
    }

    public void navigateToEmployeesPage() {
        world.driver.get(TextConstants.EmployeesPageConstants.PAGE_URL);
        String currentUrl = world.driver.getCurrentUrl();
        if(!Objects.equals(currentUrl, TextConstants.EmployeesPageConstants.PAGE_URL)){
            CalendarPage calendar = login();
            calendar.menuBar.clickEmployeesButton();
        }
    }
    private CalendarPage login(){
        log.info("Logging in as {}@{}", world.currentUser.getEmail(), world.currentUser.getRawPassword());
        world.driver.get(TextConstants.GeneralSettingsConstants.PAGE_URL);
        LoginPage loginPage = new LoginPage(world.driver);
        loginPage.fillEmail(world.currentUser.getEmail());
        loginPage.fillPassword(world.currentUser.getRawPassword());
        return loginPage.clickLoginButton();
    }

}
