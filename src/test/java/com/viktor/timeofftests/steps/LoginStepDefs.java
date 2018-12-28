package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.When;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginStepDefs {
    World world;

    public LoginStepDefs (World world){
        this.world = world;
    }


    @When("^I login as user \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void iLoginAsUserWithPassword(String email, String password) throws Throwable {
        log.info("Logging as user {}@{}", email, password);
        LoginPage loginPage = new LoginPage(this.world.driver);
        loginPage.open();
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.clickLoginButtonExpectingSuccess();
    }
}
