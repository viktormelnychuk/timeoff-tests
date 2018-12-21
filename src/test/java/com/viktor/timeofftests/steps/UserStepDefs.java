package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;

public class UserStepDefs {
    private World world;
    private UserService userService;
    private CompanyService companyService;
    private DepartmentService departmentService;
    public UserStepDefs(World world, UserService userService, CompanyService companyService, DepartmentService departmentService){
        this.world = world;
        this.userService = userService;
        this.companyService = companyService;
        this.departmentService = departmentService;
    }

    @Given("^following user is created:$")
    public void followingUserIsCreated(DataTable table) {
        User user = UserSteps.createUser(table);
        if(world.company == null){
            world.company = companyService.getOrCreateCompanyWithName(user.getCompanyName());
        }
        if(world.currentUserDepartment == null){
            world.currentUserDepartment =
                    departmentService.getOrCreateDepartmentWithName(user.getDepartmentName(), world.company.getId());
        }
        user.setCompanyID(world.company.getId());
        user.setDepartmentID(world.currentUserDepartment.getId());
        world.defaultUser = userService.createNewUser(user);
    }



}
