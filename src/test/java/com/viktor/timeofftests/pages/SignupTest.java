package com.viktor.timeofftests.pages;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SignupTest extends BaseTest {
    @Test
    public void signupAsNewUser(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage.open("http://localhost:3000/register");
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
        assertEquals(expectedEmployeeGreeting, employeeGreeting);
    }

    @Test
    public void verifyCompanyNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage.clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }
    @Test
    public void verifyFirstNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage
                .fillCompanyName("Company")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }

    @Test
    public void verifyLastNameRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage
                .fillCompanyName("Company")
                .fillFirstName("First Name")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }

    @Test
    public void verifyEmailRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("Some test")
                .fillLastName("Some test2")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }

    @Test
    public void verifyPasswordRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage
                .fillCompanyName("Test")
                .fillFirstName("some test")
                .fillLastName("Another test")
                .fillEmail("email@email.com")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }

    @Test
    public void verifyPasswordConfirmationRequired(){
        SignupPage signupPage = new SignupPage(getDriver());
        signupPage = signupPage
                .fillCompanyName("Some comp")
                .fillFirstName("Test")
                .fillLastName("test")
                .fillEmail("email@email.em")
                .fillPassword("Password")
                .clickCreateButtonExpectingFailure();
        assertEquals(signupPage.getBaseURL(),getDriver().getCurrentUrl());
    }

    @Test
    public void testing() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        String pwf = "1234!2~`HswpPPLa22+=±§sdq qwe,appp qwwokDF_";
        String t = DigestUtils.md5Hex(pwf.getBytes());
        System.out.println(t);
    }

}