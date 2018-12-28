package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.NewDepartmentForm;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class DepartmentsStepDefs {
    private DepartmentService departmentService;
    private UserService userService;
    private CompanyService companyService;
    private DepartmentsSteps departmentsSteps;
    private World world;

    public DepartmentsStepDefs(DepartmentService departmentService, UserService userService, CompanyService companyService, DepartmentsSteps departmentsSteps, World world) {
        this.departmentService = departmentService;
        this.userService = userService;
        this.companyService = companyService;
        this.departmentsSteps = departmentsSteps;
        this.world = world;
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
        System.out.println("asd");
    }
}
