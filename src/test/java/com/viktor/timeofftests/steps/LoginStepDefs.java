package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.log4j.Log4j2;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Log4j2
public class LoginStepDefs {
    World world;

    public LoginStepDefs (World world){
        this.world = world;
    }


    @When("^I login as user \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void iLoginAsUserWithPassword(String email, String password){
        log.info("Logging as user {}@{}", email, password);
        LoginPage loginPage = new LoginPage(this.world.driver);
        loginPage.open();
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.clickLoginButton();
    }

    @Then("I can login as user with {string} email and {string} password")
    public void iCanLoginAsUserWithEmailAndPassword(String email, String password) {
        log.info("Trying to login as user {}@{}",email,password);
        this.world.driver.manage().deleteAllCookies();
        this.world.driver.navigate().refresh();
        LoginPage loginPage = new LoginPage(world.driver);
        loginPage.open();
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.clickLoginButton();
        assertThat(world.driver.getCurrentUrl(), is(TextConstants.CalendarPageConstants.PAGE_URL));
    }
}
