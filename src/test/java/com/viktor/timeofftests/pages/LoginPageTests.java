package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.SessionService;
import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.Test;
import sun.rmi.runtime.Log;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginPageTests extends BaseTest {
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    @Test
    void adminCanLogin(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        User user = new User.Builder()
                .withEmail("tester@viktor.com")
                .withName("John")
                .withLastName("Doe")
                .withPassword("1234")
                .inCompany("Acme")
                .inDepartment("Sales")
                .isAdmin()
                .isAutoApproved()
                .build();
        user = userService.createNewUser(user);
        CalendarPage calendarPage = loginPage
                .fillEmail(user.getEmail())
                .fillPassword(user.getRawPassword())
                .clickLoginButtonExpectingSuccess();
        String expectedEmployeeGreeting = "John Doe's calendar for 2018";
        String sidInDriver = DriverUtil.getSidFromCookies(calendarPage.getDriver());

        assertThat(true, is(calendarPage.menuBar.employeesButtonDisplayed()));
        assertThat(calendarPage.getEmployeeGreeting(), is(expectedEmployeeGreeting));
        assertThat((sessionService.getSessionWithSid(sidInDriver)), is(notNullValue()));
    }

    @Test
    void nonAdminCanLogin(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        User user = new User.Builder()
                .withEmail("tester@viktor.com")
                .withPassword("1234")
                .withName("John")
                .withLastName("Doe")
                .inCompany("Acme")
                .inDepartment("Sales")
                .build();
        user = userService.createNewUser(user);
        CalendarPage calendarPage = loginPage
                .fillEmail(user.getEmail())
                .fillPassword(user.getRawPassword())
                .clickLoginButtonExpectingSuccess();
        String expectedEmployeeGreeting = "John Doe's calendar for 2018";
        String sidInDriver = DriverUtil.getSidFromCookies(calendarPage.getDriver());

        assertThat(false, is(calendarPage.menuBar.employeesButtonDisplayed()));
        assertThat(calendarPage.getEmployeeGreeting(), is(expectedEmployeeGreeting));
        assertThat((sessionService.getSessionWithSid(sidInDriver)), is(notNullValue()));
    }

    @Test
    void cannotLoginAsNotRegisteredUser (){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage = loginPage
                .fillEmail("non-existing@viktor.com")
                .fillPassword("1234")
                .clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl(), is(loginPage.getDriver().getCurrentUrl()));
        assertThat(loginPage.getAlertMessage(), is("Incorrect credentials"));
    }

    @Test
    void fieldsRequired(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl(), is(loginPage.getDriver().getCurrentUrl()));
        loginPage.fillEmail("email@email.email").clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl(), is(loginPage.getDriver().getCurrentUrl()));
    }

    @Test
    void canNavigateToSignupPage(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        SignupPage signupPage = loginPage.clickRegisterLink();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));
    }

}
