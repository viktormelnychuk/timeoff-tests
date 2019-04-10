package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.forms.EmployeeForm;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
@Log4j2
public class UserStepDefs {
    private World world;
    private UserService userService;
    private CompanyService companyService;
    private DepartmentService departmentService;
    private NavigationSteps navigationSteps;
    private CalendarSteps calendarSteps;
    public UserStepDefs(World world, UserService userService, CompanyService companyService, DepartmentService departmentService, NavigationSteps navigationSteps, CalendarSteps calendarSteps){
        this.world = world;
        this.userService = userService;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.navigationSteps = navigationSteps;
        this.calendarSteps = calendarSteps;
    }

    @Given("^following user is created:$")
    public void followingUserIsCreated(DataTable table) {
        log.info("Creating user {}", table);
        User user = table.convert(User.class, false);
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
        log.info("Starting to create default {} user", admin);
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
        userService.makeDepartmentAdmin(world.currentUser);
        world.currentCompany = company;
        world.currentUserDepartment = department;
        world.allDepartments.add(department);
        world.allUsers.add(world.currentUser);
        log.info("Done creating default {} user", admin);
    }

    @Given("following users are created:")
    public void followingUsersAreCreated(DataTable table) {
        log.info("Starting to create multiple users\r\n{}", table);
        List<EmployeeForm> usersToInsert = table.asList(EmployeeForm.class);
        for (EmployeeForm form: usersToInsert) {
            Department department = departmentService.getDepartmentWithNameAndCompanyId(form.getDepartmentName(), world.currentCompany.getId());
            User user = new User.Builder()
                    .withName(form.getFirstName())
                    .withEmail(form.getEmail())
                    .withLastName(form.getLastName())
                    .withPassword(form.getPassword())
                    .inCompany(world.currentCompany)
                    .inDepartment(department.getId())
                    .admin(form.isAdmin())
                    .autoApproved(form.isAutoApprove())
                    .startedOn(form.getStartedOn())
                    .endedOn(form.getEndedOn())
                    .build();
            world.allUsers.add(userService.createNewUser(user));
        }
        log.info("Done creating multiple users");
    }

    @Then("user with email {string} should see correct info")
    public void userWithEmailShouldSeeCorrectInfo(String email) throws Exception {
        User user = userService.getUserWithEmail(email);
        if(StringUtils.isEmpty(world.editedUserForm.getPassword())){
            navigationSteps.navigateWithoutActualLogin(user, TextConstants.CalendarPageConstants.PAGE_URL);
        } else {
            navigationSteps.navigateWithoutActualLogin(user,TextConstants.CalendarPageConstants.PAGE_URL);
        }
        calendarSteps.validateCalendarPage(user);
    }
}
