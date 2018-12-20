package com.viktor.timeofftests.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

public class StepDefs {
    private String today;
    private String actualAnswer;

    @Given("^today is \"([^\"]*)\"$")
    public void today_is_Sunday(String today){

    }
    @Given("^today is Friday$")
    public void today_is_Friday() {

    }
    @When("^I ask whether it is Friday$")
    public void i_ask_whether_it_is_Friday(){

    }

    @Then("^I should be told \"([^\"]*)\"$")
    public void i_should_be_told(String expectedAnswer){
    }

    @Given("^following users are created:$")
    public void followingUsersAreCreated() {

    }

    @Given("^company with name \"([^\"]*)\" is created$")
    public void companyWithNameIsCreated(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
