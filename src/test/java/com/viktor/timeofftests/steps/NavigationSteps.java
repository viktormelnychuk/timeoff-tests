package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.pages.EmployeesPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import lombok.extern.log4j.Log4j2;


import static org.junit.Assert.*;
import static com.viktor.timeofftests.common.DriverUtil.simulateLoginForUser;
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
                navigateWithoutActualLogin(TextConstants.CalendarPageConstants.PAGE_URL);
                break;
            case Pages.LOGIN:
                world.driver.get(TextConstants.LoginPageConstants.PAGE_URL);
                break;
            case Pages.REGISTER:
                world.driver.get(TextConstants.RegisterPageConstants.PAGE_URL);
                break;
            case Pages.SETTINGS:
                navigateWithoutActualLogin(TextConstants.GeneralSettingsConstants.PAGE_URL);
                break;
            case Pages.DEPARTMENTS:
                navigateWithoutActualLogin(TextConstants.DepartmentsConstants.PAGE_URL);
                break;
            case Pages.EMPLOYEES:
                navigateWithoutActualLogin(TextConstants.EmployeesPageConstants.PAGE_URL);
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
        navigateWithoutActualLogin(TextConstants.DepartmentsConstants.PAGE_URL);
        DepartmentsPage page = new DepartmentsPage(world.driver);
        page.clickDepartmentLink(departmentName);
    }
    public void navigateWithoutActualLogin(String pageUrl){
        simulateLoginForUser(world.currentUser.getId(), world.driver);
        world.driver.get(pageUrl);
    }
    public void navigateToAddEmployeePage() {
        navigateWithoutActualLogin(TextConstants.AddNewEmployeePage.PAGE_URL);
        new EmployeesPage(world.driver)
                .clickAddSingleEmployeeButton();


    }

    public void navigateWithoutActualLogin(User user, String pageUrl) {
        simulateLoginForUser(user.getId(), world.driver);
        world.driver.get(pageUrl);
    }
}
