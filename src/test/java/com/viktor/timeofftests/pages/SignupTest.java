package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

//TODO: Refactor all assertions to AssertThat;

@RunWith(JUnit4.class)
public class SignupTest extends BaseTest {
    UserService userService = UserService.getInstance();
    @Test
    public void signupAsNewUser(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        CalendarPage calendarPage = signupPage
                .fillCompanyName("TestCompany")
                .fillFirstName("First Name")
                .fillLastName("Last Name")
                .fillEmail("email@email.tes")
                .fillPassword("1234")
                .fillPasswordConfirmation("1234")
                .selectCountry("CU: Cuba")
                .selectTimeZone("Europe/Kirov")
                .clickCreateButtonExpectingSuccess();
        String alertMessage = calendarPage.getAlertMessage();
        String employeeGreeting = calendarPage.getEmployeeGreeting();
        String expectedEmployeeGreeting = "First Name Last Name's calendar for 2018";
        assertEquals("Registration is complete.", alertMessage);
        assertTrue("User is not an admin", userService.userIsAdmin("email@email.tes"));
        assertEquals(expectedEmployeeGreeting, employeeGreeting);
    }

    @Test
    public void verifyCompanyNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }
    @Test
    public void verifyFirstNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Company")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }

    @Test
    public void verifyLastNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Company")
                .fillFirstName("First Name")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }

    @Test
    public void verifyEmailRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("Some test")
                .fillLastName("Some test2")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }

    @Test
    public void verifyPasswordRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("some test")
                .fillLastName("Another test")
                .fillEmail("email@email.com")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }

    @Test
    public void verifyPasswordConfirmationRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Some comp")
                .fillFirstName("Test")
                .fillLastName("test")
                .fillEmail("email@email.em")
                .fillPassword("Password")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseUrl(),signupPage.getDriver().getCurrentUrl());
    }

    @Test
    public void verifyCannotLoginWithExistingUser(){
        User user = new User.Builder()
                .withEmail("email@er.re")
                .withName("name")
                .withLastName("lastname")
                .withPassword("1234")
                .inCompany("Test Company")
                .inDepartment("Department1")
                .buildAndStore();
        UserService.getInstance().makeDepartmentAdmin(user);
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage.signupWithUserExpectingFailure(user);
        assertEquals("Failed to register user please contact customer service. Error: Email is already used", signupPage.getAlertMessage());
    }

    @Test
    public void verifyCanLoginAfterRegistration(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        CalendarPage calendarPage = signupPage.signupAsDefaultUser();
        LoginPage loginPage = calendarPage.menuBar.logout();
        calendarPage = loginPage.loginWithDefaultUser();
        assertEquals(calendarPage.getBaseUrl(), calendarPage.getDriver().getCurrentUrl());
        String expectedEmployeeGreeting = Constants.DEFAULT_USER_NAME + " " +Constants.DEFAULT_USER_LAST_NAME + "'s calendar for 2018";
        assertEquals(expectedEmployeeGreeting, calendarPage.getEmployeeGreeting());

    }
}