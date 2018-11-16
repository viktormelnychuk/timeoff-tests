package com.viktor.timeofftests.pages;


import com.viktor.timeofftests.common.DBUtil;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SignupTest {
    private WebDriver driver;
    @BeforeEach
    void setUp(){
        if(driver == null) {
            this.driver = DriverUtil.getDriver(DriverEnum.CHROME);
        }
    }

    @BeforeAll
    static void cleanDB(){
        DBUtil.cleanDB();
    }


    @Test
    void signupAsNewUser() throws Exception {
        SignupPage signupPage = new SignupPage(this.driver);
        CalendarPage calendarPage = signupPage
                .fillCompanyName("TestCompany")
                .fillFirstName("First Name")
                .fillLastName("Last Name")
                .fillEmail("email@email.tes")
                .fillPassword("1234")
                .fillPasswordConfirmation("1234")
                .selectCountry("CU: Cuba123123")
                .selectTimeZone("Europe/Kirov")
                .clickCreateButtonExpectingSuccess();
        String alertMessage = calendarPage.getAlertMessage();
        String employeeGreeting = calendarPage.getEmployeeGreeting();
        String expectedEmployeeGreeting = "First Name Last Name's calendar for 2018";
        assertEquals("Registration is complete.", alertMessage);
        assertEquals(expectedEmployeeGreeting, employeeGreeting);
    }

    @Test
    void verifyCompanyNameRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }
    @Test
    void verifyFirstNameRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage
                .fillCompanyName("Company")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }

    @Test
    void verifyLastNameRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage
                .fillCompanyName("Company")
                .fillFirstName("First Name")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }

    @Test
    void verifyEmailRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("Some test")
                .fillLastName("Some test2")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }

    @Test
    void verifyPasswordRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("some test")
                .fillLastName("Another test")
                .fillEmail("email@email.com")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }

    @Test
    void verifyPasswordConfirmationRequired(){
        SignupPage signupPage = new SignupPage(this.driver);
        signupPage = signupPage
                .fillCompanyName("Some comp")
                .fillFirstName("Test")
                .fillLastName("test")
                .fillEmail("email@email.em")
                .fillPassword("Password")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),this.driver.getCurrentUrl());
    }

    @AfterEach
    void tearDown(){
        this.driver.close();
    }
}
