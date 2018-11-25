package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.SessionService;
import com.viktor.timeofftests.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(JUnit4.class)
public class LoginPageTests extends BaseTest {
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    @Test
    public void adminCanLogin(){
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
        //Assert that menu bar has employees buttons ETC
        assertThat(calendarPage.getEmployeeGreeting(), is(expectedEmployeeGreeting));
        assertThat((sessionService.getSessionWithSid(sidInDriver)), is(notNullValue()));
    }

}
