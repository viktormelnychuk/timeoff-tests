package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import cucumber.api.java.en.Given;

public class DepartmentStepDefs {

    World world;
    CompanyService companyService;
    DepartmentService departmentService;
    public DepartmentStepDefs (World world, CompanyService companyService, DepartmentService departmentService){
        this.world = world;
        this.companyService = companyService;
        this.departmentService = departmentService;
    }

    @Given("default department {string} in {string} company is created")
    public void defaultDepartmentInCompanyIsCreated(String departmentName, String companyName) {
        Company company = companyService.getCompanyWithName(companyName);
        world.currentUserDepartment = departmentService.getOrCreateDepartmentWithName(departmentName, company.getId());
    }
}
