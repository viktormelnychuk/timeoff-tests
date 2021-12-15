package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.ScheduleService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Then;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
@Log4j2
public class DatabaseVerificationStepDefs {
    private World world;
    private SessionSteps sessionSteps;
    private CompanyService companyService;
    private DepartmentService departmentService;
    private UserService userService;
    private ScheduleService scheduleService;
    public DatabaseVerificationStepDefs(World world, SessionSteps sessionSteps, CompanyService companyService, DepartmentService departmentService, UserService userService, ScheduleService scheduleService){
        this.world = world;
        this.sessionSteps = sessionSteps;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    @Then("^database should (have|not have) session associated with \"([^\"]*)\"$")
    public void databaseShouldHaveSessionAssociatedWith(String should ,String email) throws Throwable {
        switch (should){
            case "have":
                log.info("Verifying that session for user [{}] exists", email);
                sessionSteps.sessionPresent(email);
                break;
            case "not have":
                log.info("Verifying that session for user [{}] does not exists", email);
                sessionSteps.sessionIsNotPresent(email);
                break;
            default:
                throw new Exception("Bad step");
        }

    }

    @Then("^company with name \"([^\"]*)\" (should|should not) be present in database$")
    public void companyWithNameShouldBePresentInDatabase(String companyName, String should) {
        Company company = companyService.getCompanyWithName(companyName);
        if(should.equals("should")){
            log.info("Verifying company with name [{}] exists in database", companyName);
            assertNotNull(company);
        } else {
            log.info("Verifying company with name [{}] does not exists in database", companyName);
            assertNull(company);
        }

    }

    @Then("department with name \"([^\"]*)\" should be present in database")
    public void departmentWithNameShouldBePresentInDatabase(String departmentName) {
        Department department = departmentService.getDepartmentWithName(departmentName);
        log.info("Verifying department with name [{}] exists in database", departmentName);
        assertNotNull("department was not found", department);
    }
    @Then("department with name \"([^\"]*)\" should not be present in database")
    public void departmentWithNameShouldNotBePresentInDatabase(String departmentName) {
        Department department = departmentService.getDepartmentWithName(departmentName);
        log.info("Verifying department with name [{}] does not exist in database", departmentName);
        assertNotNull("department was found", department);
    }


    @Then("^user \"([^\"]*)\" should be in \"([^\"]*)\" company and \"([^\"]*)\" department$")
    public void userShouldBeInCompanyAndDepartment(String email, String companyName, String departmentName) {
        log.info("Verifying that user [{}] belongs to company [{}] and department [{}]",email, companyName, departmentName);
        Company company = companyService.getCompanyWithName(companyName);
        Department department = departmentService.getDepartmentWithName(departmentName);
        User user = userService.getUserWithEmail(email);
        assertNotNull("user was not found", user);
        assertEquals(user.getCompanyID(), company.getId());
        assertEquals(user.getDepartmentID(), department.getId());
    }

    @Then("user \"([^\"]*)\" should not be present in database")
    public void userShouldNotBePresentInDatabase(String email) {
        log.info("Verifying user with email [{}] does not exists in database", email);
        User user = userService.getUserWithEmail(email);
        assertNull("user was found when shouldn't be ",user);
    }

    @Then("database should have correct weekly schedule associated with company {string}")
    public void databaseShouldHaveCorrectWeeklyScheduleAssociatedWithCompany(String arg0) throws Exception {
        throw new Exception("Fix this method");
    }

    @Then("^company with name \"([^\"]*)\" is deleted$")
    public void companyWithNameIsDeleted(String arg0) {
        Company company = companyService.getCompanyWithName(arg0);
        log.info("Verifying company with name [{}] does not exist", arg0);
        assertThat(company, is(nullValue()));
    }

    @Then("company with name {string} is not deleted")
    public void companyWithNameIsNotDeleted(String arg0) {
        log.info("Verifying company with name [{}] exist", arg0);
        Company company = companyService.getCompanyWithName(arg0);
        assertThat(company, is(notNullValue()));
    }


    @Then("user is {string}")
    public void userIs(String resultOfDeletion) {
        if(StringUtils.equals(resultOfDeletion,"deleted")){
            User userWithId = userService.getUserWithId(world.deletedUserId);
            assertThat(userWithId, nullValue());
        } else if (StringUtils.equals(resultOfDeletion,"not deleted")){
            User userWithId = userService.getUserWithId(world.deletedUserId);
            assertThat(userWithId, notNullValue());
        }
    }
}
