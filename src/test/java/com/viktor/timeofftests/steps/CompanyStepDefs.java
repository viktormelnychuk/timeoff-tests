package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.services.CompanyService;
import cucumber.api.java.en.Given;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CompanyStepDefs {
    private World world;
    private CompanyService companyService;
    public CompanyStepDefs(World world, CompanyService companyService){
        this.world = world;
        this.companyService = companyService;
    }


    @Given("^default company with name \"([^\"]*)\" is created$")
    public void defaultCompanyWithNameIsCreated(String arg0) {
        log.info("Creating default company with name {}", arg0);
        world.currentCompany = companyService.getOrCreateCompanyWithName(arg0);
    }
}
