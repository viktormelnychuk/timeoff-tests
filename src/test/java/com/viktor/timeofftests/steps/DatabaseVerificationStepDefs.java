package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.services.CompanyService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class DatabaseVerificationStepDefs {
    private World world;
    private SessionSteps sessionSteps;
    private CompanyService companyService;
    public DatabaseVerificationStepDefs(World world, SessionSteps sessionSteps, CompanyService companyService){
        this.world = world;
        this.sessionSteps = sessionSteps;
        this.companyService = companyService;
    }

    @Then("^database should (have|not have) session associated with \"([^\"]*)\"$")
    public void databaseShouldHaveSessionAssociatedWith(String should ,String email) throws Throwable {
        switch (should){
            case "have":
                sessionSteps.sessionPresent(email);
                break;
            case "not have":
                sessionSteps.sessionIsNotPresent(email);
                break;
            default:
                throw new Exception("Bad step");
        }

    }
}
