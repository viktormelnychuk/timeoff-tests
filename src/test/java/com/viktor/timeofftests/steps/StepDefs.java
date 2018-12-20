package com.viktor.timeofftests.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefs {
    @Given("^default company with name \"([^\"]*)\" is created$")
    public void defaultCompanyWithNameIsCreated(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^following user is created:$")
    public void followingUserIsCreated() {

    }

    @When("^I login as user \"([^\"]*)\" with password \"([^\"]*)\"$")
    public void iLoginAsUserWithPassword(String arg0, String arg1) throws Throwable {

        throw new PendingException();
    }


    @And("^the \"([^\"]*)\" page should be opened$")
    public void pageShouldBeOpened(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^database should have session associated with \"([^\"]*)\"$")
    public void databaseShouldHaveSessionAssociatedWith(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
