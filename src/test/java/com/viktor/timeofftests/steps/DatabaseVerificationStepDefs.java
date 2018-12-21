package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import cucumber.api.java.en.Then;

public class DatabaseVerificationStepDefs {
    private World world;
    private SessionSteps sessionSteps;

    public DatabaseVerificationStepDefs(World world, SessionSteps sessionSteps){
        this.world = world;
        this.sessionSteps = sessionSteps;
    }

    @Then("^database should have session associated with \"([^\"]*)\"$")
    public void databaseShouldHaveSessionAssociatedWith(String email) throws Throwable {
        sessionSteps.sessionPresent(email);
    }
}
