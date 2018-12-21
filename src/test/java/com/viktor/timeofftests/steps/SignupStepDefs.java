package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.pages.SignupPage;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.List;

public class SignupStepDefs {

    private World world;

    public SignupStepDefs(World world) {
        this.world = world;
    }

    @When("I signup as:")
    public void iSignupAs(DataTable table) {
        SignupPage signupPage = new SignupPage(world.driver);
        List<String> row = table.row(1);
        signupPage.fillCompanyName(row.get(0));
        signupPage.fillEmail(row.get(1));
        signupPage.fillFirstName(row.get(2));
        signupPage.fillLastName(row.get(3));
        signupPage.fillPassword(row.get(4));
        signupPage.fillPasswordConfirmation(row.get(4));
        signupPage.clickCreateButtonExpectingSuccess();
    }
}
