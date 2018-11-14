package com.viktor.timeofftests.pages;


import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SignupTest {
    private WebDriver driver;
    @BeforeEach
    void setUp(){
        this.driver = DriverUtil.getDriver(DriverEnum.CHROME);
    }


    @Test
    void signupAsNewUser(){
        SignupPage signupPage = new SignupPage(this.driver);
        CalendarPage calendarPage = signupPage
                .fillCompanyName("TestCompany")
                .fillFirstName("First Name")
                .fillLastName("Last Name")
                .fillEmail("email@email.tes")
                .fillPassword("1234")
                .fillPasswordConfirmation("1234")
                .selectCountry("GB: United Kingdom")
                .selectTimeZone("Europe/London")
                .clickCreateButton();
        String alertMessage = calendarPage.getAlertMessage();
        String employeeGreeting = calendarPage.getEmployeeGreeting();
        String expectedEmployeeGreeting = "First Name Last Name's calendar for 2018";
        assertEquals("Registration is complete.", alertMessage);
        assertEquals(expectedEmployeeGreeting, employeeGreeting);
    }
    @AfterEach
    void tearDown(){
        //this.driver.close();
    }
}
