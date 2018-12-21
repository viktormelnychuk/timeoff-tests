package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.And;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UIVerificationStepDefs {

    private World world;

    public UIVerificationStepDefs(World world){
        this.world = world;
    }

    @And("^the \"([^\"]*)\" page should be opened$")
    public void pageShouldBeOpened(String page) throws Exception {
        switch (page.toLowerCase()){
            case Pages.CALENDAR:
                verifyCalendarPageTexts();
                break;
            case Pages.LOGIN:
                verifyLoginPage();
                break;
            default:
                throw new Exception("Page " + page + " is not present in app");
        }
    }

    private void verifyLoginPage() {
        LoginPage page = new LoginPage(world.driver);
        assertTrue("Current url", page.getDriver().getCurrentUrl().contains(TextConstants.LoginPageConstants.PAGE_URL));
        assertEquals("Error message", TextConstants.LoginPageConstants.ERROR_MESSAGE, page.getAlertMessage());
    }


    private void verifyCalendarPageTexts(){
        CalendarPage page = new CalendarPage(world.driver);
        String expectedGreeting = String.format(TextConstants.CalendarPageConstants.EMPLOYEE_GREETING_F,
                world.defaultUser.getName(), world.defaultUser.getLastName());
        assertEquals("Employee greeting does not match expected", page.getEmployeeGreeting(), expectedGreeting);
        assertEquals("Current url", TextConstants.CalendarPageConstants.PAGE_URL, page.getDriver().getCurrentUrl());
        assertEquals("Page title", TextConstants.CalendarPageConstants.PAGE_TITLE, page.getTitle());
    }

}
