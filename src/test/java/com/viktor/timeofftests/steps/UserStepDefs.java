package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Objects;

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
        if(world.currentCompany == null){
            world.currentCompany = companyService.getOrCreateCompanyWithName(user.getCompanyName());
        }
        if(world.currentUserDepartment == null){
            world.currentUserDepartment =
                    departmentService.getOrCreateDepartmentWithName(user.getDepartmentName(), world.currentCompany.getId());
        }
        user.setCompanyID(world.currentCompany.getId());
        user.setDepartmentID(world.currentUserDepartment.getId());
        world.currentUser = userService.createNewUser(user);
    }


    @Given("^default \"([^\"]*)\" user is created$")
    public void defaultUserIsCreated(String admin) {
        Company company = companyService.getOrCreateCompanyWithName(Constants.DEFAULT_COMPANY_NAME);
        Department department = departmentService.getOrCreateDepartmentWithName(Constants.DEFAULT_DEPARTMENT_NAME, company.getId());
        User user = new User.Builder()
                .withEmail(Constants.DEFAULT_USER_EMAIL)
                .withLastName(Constants.DEFAULT_USER_LAST_NAME)
                .withName(Constants.DEFAULT_USER_NAME)
                .withPassword(Constants.DEFAULT_USER_PASSWORD)
                .inCompany(company)
                .inDepartment(department)
                .build();
        if(Objects.equals(admin, "admin")){
            user.setAdmin(true);
        }
        world.currentUser = userService.createNewUser(user);
        world.currentCompany = company;
        world.currentUserDepartment = department;
        world.allDepartments.add(department);
        world.allUsers.add(world.currentUser);
    }

    @Given("following users are created:")
    public void followingUsersAreCreated(DataTable table) {
        List<User> users = table.asList(User.class);
        world.allUsers.addAll(userService.createUsersInDepartmentAndCompany(users, world.currentUserDepartment.getId(), world.currentCompany.getId()));
        System.out.println(users);
    }
}
