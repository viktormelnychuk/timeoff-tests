package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.LoginPage;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class UserStepDefs {
    private World world;

    public UserStepDefs (World world){
        this.world = world;
    }

    @Given("^following user is created:$")
    public void followingUserIsCreated(DataTable table) {
        User user = UserSteps.createUser(table);
        user.setCompanyID(world.company.getId());
        world.defaultUser = UserService.getInstance().createNewUser(user);
    }



}
