package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
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
