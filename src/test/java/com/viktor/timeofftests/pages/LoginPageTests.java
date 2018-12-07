package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.SessionService;
import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPageTests extends BaseTest {
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    @Test
    void adminCanLogin(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        User user = new User.Builder()
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
        assertThat(calendarPage.menuBar.employeesButtonDisplayed()).isTrue();
        assertThat(calendarPage.getEmployeeGreeting()).isEqualTo(expectedEmployeeGreeting);
        assertThat(sessionService.getSessionWithSid(sidInDriver)).isNotNull();
    }

    @Test
    void nonAdminCanLogin(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        User user = new User.Builder()
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

        assertThat(calendarPage.menuBar.employeesButtonDisplayed()).isFalse();
        assertThat(calendarPage.getEmployeeGreeting()).isEqualTo(expectedEmployeeGreeting);
        assertThat((sessionService.getSessionWithSid(sidInDriver))).isNotNull();
    }

    @Test
    void cannotLoginAsNotRegisteredUser (){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage = loginPage
                .fillEmail("non-existing@viktor.com")
                .fillPassword("1234")
                .clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl()).isEqualTo(loginPage.getDriver().getCurrentUrl());
        assertThat(loginPage.getAlertMessage()).isEqualTo("Incorrect credentials");
    }

    @Test
    void fieldsRequired(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        loginPage.clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl()).isEqualTo(loginPage.getDriver().getCurrentUrl());
        loginPage.fillEmail("email@email.email").clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl()).isEqualTo(loginPage.getDriver().getCurrentUrl());
    }

    @Test
    void cannotLoginAsDeactivatedUser(){
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_YEAR, -10);
        // Roll 10 days back from today
        Date startedOn = calendar.getTime();

        // Roll 9 days from startedOn
        calendar.roll(Calendar.DAY_OF_YEAR, 9);
        Date endedOn = calendar.getTime();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        User user = new User.Builder()
                .inCompany("Acme")
                .inDepartment("Sales")
                .startedOn(startedOn)
                .endedOn(endedOn)
                .build();
        user = userService.createNewUser(user);
        loginPage
                .fillEmail(user.getEmail())
                .fillPassword(user.getRawPassword())
                .clickLoginButtonExpectingFailure();
        assertThat(loginPage.getBaseUrl()).isEqualTo(loginPage.getDriver().getCurrentUrl());
        assertThat(loginPage.getAlertMessage()).isEqualTo("Incorrect credentials");
    }

    @Test
    void canNavigateToSignupPage(){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open();
        SignupPage signupPage = loginPage.clickRegisterLink();
        assertThat(signupPage.getBaseUrl()).isEqualTo(loginPage.getDriver().getCurrentUrl());
    }

}
