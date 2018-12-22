package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.forms.SignupForm;
import com.viktor.timeofftests.pages.SignupPage;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class SignupStepDefs {

    private World world;
    private UserService userService;

    public SignupStepDefs(World world, UserService userService) {
        this.world = world;
        this.userService = userService;
    }

    @When("^I signup as:$")
    public void iSignupAs(DataTable table) {
        SignupForm form = table.convert(SignupForm.class, false);
        SignupPage signupPage = new SignupPage(world.driver);
        signupPage.fillCompanyName(form.getCompanyName());
        signupPage.fillEmail(form.getEmail());
        signupPage.fillFirstName(form.getFirstName());
        signupPage.fillLastName(form.getLastName());
        signupPage.fillPassword(form.getPassword());
        signupPage.fillPasswordConfirmation(form.getPasswordConfirmation());
        signupPage.clickCreateButtonExpectingSuccess();
        if(!world.driver.getCurrentUrl().contains(TextConstants.RegisterPageConstants.PAGE_URL)){
            world.currentUser = userService.getUserWithEmail(form.getEmail());
        }
    }
}
