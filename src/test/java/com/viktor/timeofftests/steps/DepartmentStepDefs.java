package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.NewDepartmentForm;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DepartmentStepDefs {

    private World world;
    private CompanyService companyService;
    private DepartmentService departmentService;
    private UserService userService;
    private DepartmentsSteps departmentsSteps;

    public DepartmentStepDefs(World world, CompanyService companyService, DepartmentService departmentService, UserService userService, DepartmentsSteps departmentsSteps){
        this.world = world;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.departmentsSteps = departmentsSteps;
    }

    @Given("default department {string} in {string} company is created")
    public void defaultDepartmentInCompanyIsCreated(String departmentName, String companyName) {
        Company company = companyService.getCompanyWithName(companyName);
        world.currentUserDepartment = departmentService.getOrCreateDepartmentWithName(departmentName, company.getId());
    }
    @Given("following departments are created:")
    public void followingDepartmentsAreCreated(DataTable dataTable) throws Exception {
        int companyId = 0;
        List<NewDepartmentForm> form = dataTable.asList(NewDepartmentForm.class);
        for (NewDepartmentForm item : form) {
            if(StringUtils.isNotEmpty(item.getCompanyName())){
                companyId = companyService.getCompanyWithName(item.getCompanyName()).getId();
            } else {
                companyId = world.currentCompany.getId();
            }
            Department department = departmentService.createDepartment(new Department.Builder()
                    .withName(item.getName())
                    .withAllowance(item.getAllowance())
                    .isAccuredAllowance(item.isAccruedAllowance())
                    .includePublicHolidays(item.isPublicHolidays())
                    .inCompany(companyId)
                    .build());
            userService.createRandomUsersInDepartment(department.getId(), item.getNumberOfUsers());
            this.world.allDepartments.add(department);
        }
        departmentService.setBossesForAllDepartments(companyId);
        departmentsSteps.validateDepartmentsPresent(world.allDepartments, companyId);
    }

    @Then("departments page reflects correct information")
    public void departmentsPageReflectsCorrectInformation() {
        //TODO: implement step
        System.out.println("asd");
    }

    @When("I create following department:")
    public void iCreateFollowingDepartment() {
        //TODO: implement step
    }
}
