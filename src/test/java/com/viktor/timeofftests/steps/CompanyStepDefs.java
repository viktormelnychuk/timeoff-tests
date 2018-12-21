package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.services.CompanyService;
import cucumber.api.java.en.Given;

public class CompanyStepDefs {
    private World world;
    private CompanyService companyService;
    public CompanyStepDefs(World world, CompanyService companyService){
        this.world = world;
        this.companyService = companyService;
    }


    @Given("^default company with name \"([^\"]*)\" is created$")
    public void defaultCompanyWithNameIsCreated(String arg0) {
        world.company = companyService.getOrCreateCompanyWithName(arg0);
    }
}
