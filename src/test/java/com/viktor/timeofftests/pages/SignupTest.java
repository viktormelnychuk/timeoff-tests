package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;

@RunWith(JUnit4.class)
public class SignupTest extends BaseTest {
    private UserService userService = UserService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();
    private DepartmentService departmentService = DepartmentService.getInstance();
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

        assertThat(alertMessage, is("Registration is complete."));
        assertThat(expectedEmployeeGreeting, is(employeeGreeting));
        assertThat(userService.userIsAdmin("email@email.tes"), is(true));
        assertThat(companyService.getCompanyWithName("TestCompany"), is(notNullValue()));
        assertThat(departmentService.getDepartmentWithName("Sales"), is(notNullValue()));
    }

    @Test
    public void verifyFieldsRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));

        signupPage.fillCompanyName("Company");
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));

        signupPage.fillEmail("email@viktor,.com");
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));

        signupPage.fillFirstName("John");
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));

        signupPage.fillLastName("Doe");
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));

        signupPage.fillPassword("1234");
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertThat(signupPage.getBaseUrl(), is(signupPage.getDriver().getCurrentUrl()));
    }

    @Test
    public void verifyPasswordAndPasswordConfirmationAreSame(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage
                .fillCompanyName("Acme")
                .fillEmail("tester@viktor.com")
                .fillFirstName("Jhon")
                .fillLastName("Doe")
                .fillPassword("1234")
                .fillPasswordConfirmation("3214")
                .clickCreateButtonExpectingFailure();
        assertThat(signupPage.getAlertMessage(), is("Confirmed password does not match initial one"));
    }

    @Test
    public void verifyCannotSignupWithExistingUser(){
        User user = new User.Builder()
                .withEmail("email@er.re")
                .withName("name")
                .withLastName("lastname")
                .withPassword("1234")
                .inCompany("Test Company")
                .inDepartment("Department1")
                .build();
        userService.createNewUser(user);
        userService.makeDepartmentAdmin(user);
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        signupPage = signupPage.signupWithUserExpectingFailure(user);
        String expectedMessage = "Failed to register user please contact customer service. Error: Email is already used";
        assertThat(signupPage.getAlertMessage(), is(expectedMessage));
    }

    @Test
    public void verifyCanLoginAfterSignup(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open();
        CalendarPage calendarPage = signupPage.signupAsDefaultUser();
        LoginPage loginPage = calendarPage.menuBar.logout();
        calendarPage = loginPage.loginWithDefaultUser();
        assertThat(calendarPage.getBaseUrl(), equalTo(calendarPage.getDriver().getCurrentUrl()));
        String expectedEmployeeGreeting = Constants.DEFAULT_USER_NAME + " " +Constants.DEFAULT_USER_LAST_NAME + "'s calendar for 2018";
        assertThat(expectedEmployeeGreeting, is(calendarPage.getEmployeeGreeting()));

    }
}