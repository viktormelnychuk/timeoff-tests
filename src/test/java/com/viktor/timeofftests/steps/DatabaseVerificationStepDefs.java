package com.viktor.timeofftests.steps;

import cucumber.api.java.en.Then;

public class DatabaseVerificationStepDefs {

    @Then("^database should have session associated with \"([^\"]*)\"$")
    public void databaseShouldHaveSessionAssociatedWith(String arg0) throws Throwable {
        System.out.println("asd");
    }
}
