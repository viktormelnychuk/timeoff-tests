package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.pages.LoginPage;
import cucumber.api.java.en.When;

public class LoginStepDefs {
    World world;

    public LoginStepDefs (World world){
        this.world = world;
    }


    @When("^I login as user \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void iLoginAsUserWithPassword(String email, String password) throws Throwable {
        LoginPage loginPage = new LoginPage(this.world.driver);
        loginPage.open();
        loginPage.fillEmail(email);
        loginPage.fillPassword(password);
        loginPage.clickLoginButtonExpectingSuccess();
    }
}
