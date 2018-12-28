package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import javax.xml.soap.Text;
import java.util.Objects;

import static org.junit.Assert.*;

public class NavigationSteps {

    private World world;

    public NavigationSteps (World world){
        this.world = world;
    }

    @Given("I am on {string} page")
    public void iAmOnPage(String page) throws Exception {
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
            default:
                throw new Exception("Page was not found");
        }
    }

    private void navigateToGeneralSettings() {
        String currentUrl = world.driver.getCurrentUrl();
        if(!Objects.equals(currentUrl, TextConstants.GeneralSettingsConstants.PAGE_URL)){
            world.driver.get(TextConstants.GeneralSettingsConstants.PAGE_URL);
            LoginPage loginPage = new LoginPage(world.driver);
            loginPage.fillEmail(world.currentUser.getEmail());
            loginPage.fillPassword(world.currentUser.getRawPassword());
            CalendarPage calendarPage = loginPage.clickLoginButtonExpectingSuccess();
            calendarPage.menuBar.navigateToGeneralSettings();
        }
    }
    private void navigateToDepartmentsPage(){
        String currentUrl = world.driver.getCurrentUrl();
        if(!Objects.equals(currentUrl, TextConstants.DepartmentsConstants.PAGE_URL)){
            world.driver.get(TextConstants.LoginPageConstants.PAGE_URL);
            LoginPage loginPage = new LoginPage(world.driver);
            loginPage.fillEmail(world.currentUser.getEmail());
            loginPage.fillPassword(world.currentUser.getRawPassword());
            CalendarPage calendarPage = loginPage.clickLoginButtonExpectingSuccess();
            calendarPage.menuBar.navigateToDepartments();
        }
    }

    @And("I should be on {string} page")
    public void iShouldBeOnPage(String page) throws Exception {
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
}
