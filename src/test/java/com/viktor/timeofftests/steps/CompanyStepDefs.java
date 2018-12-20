package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.services.CompanyService;
import cucumber.api.java.en.Given;

public class CompanyStepDefs {
    private World world;

    public CompanyStepDefs(World world){
        this.world = world;
    }


    @Given("^default company with name \"([^\"]*)\" is created$")
    public void defaultCompanyWithNameIsCreated(String arg0) {
        world.company = CompanyService.getInstance().getOrCreateCompanyWithName(arg0);
    }
}
