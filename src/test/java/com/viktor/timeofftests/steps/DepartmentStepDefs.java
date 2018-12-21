package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import cucumber.api.java.en.Given;

public class DepartmentStepDefs {

    World world;

    public DepartmentStepDefs (World world){
        this.world = world;
    }

    @Given("default department {string} in {string} company is created")
    public void defaultDepartmentInCompanyIsCreated(String departmentName, String companyName) {
        Company company = CompanyService.getInstance().getCompanyWithName(companyName);
        DepartmentService.getInstance().getOrCreateDepartmentWithName(departmentName, company.getId());
    }
}
