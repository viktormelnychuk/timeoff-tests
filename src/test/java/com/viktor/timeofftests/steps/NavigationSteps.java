package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

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
            default:
                throw new Exception("Page was not found");
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
