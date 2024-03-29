package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.forms.EditEmployeeForm;
import com.viktor.timeofftests.forms.EmployeeForm;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.*;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Log4j2
public class EmployeesStepDefs {

    private World world;
    private EmployeesSteps employeesSteps;
    private NavigationSteps navigationSteps;
    private UserService userService;
    private UserSteps userSteps;
    private DepartmentService departmentService;

    public EmployeesStepDefs(World world, EmployeesSteps employeesSteps, NavigationSteps navigationSteps, UserService userService, UserSteps userSteps, DepartmentService departmentService) {
        this.world = world;
        this.employeesSteps = employeesSteps;
        this.navigationSteps = navigationSteps;
        this.userService = userService;
        this.userSteps = userSteps;
        this.departmentService = departmentService;
    }

    @When("I filter list of employees by {string} department")
    public void iFilterListOfEmployeesByDepartment(String departmentName) {
       log.info("Filtering employess by department name [{}]", departmentName);
        EmployeesPage page = new EmployeesPage(world.driver);
        page.filterByDepartmentName(departmentName);
    }

    @Then("employees page should display users that belong to {string} department")
    public void employeesPageShouldDisplayedUsersThatBelongToDepartment(String departmentName) throws Exception {
        if(StringUtils.equals(departmentName, TextConstants.EmployeesPageConstants.ALL_DEPARTMENTS_NAME)){
            employeesSteps.validateEmployeesTable();
        } else {
            employeesSteps.validateFilteredEmployeesTable(departmentName);
        }
    }

    @When("^I create an employee with following:$")
    public void iCreateAnEmployeeWithFollowing(DataTable table) throws Exception {
        log.info("Creating employee with following \n{}", table);
        navigationSteps.navigateToAddEmployeePage();
        EmployeeForm form = table.convert(EmployeeForm.class, false);
        NewEmployeePage page = new NewEmployeePage(world.driver);
        NewEmployeePage filledPage = (NewEmployeePage) fillEmployeeForm(form, page);
        filledPage.clickCreateButton();
        employeesSteps.validateEmployeeCreated(form, world.currentCompany.getId());
    }

    @When("I try to create an employee with following:")
    public void iTryToCreateAnEmployeeWithFollowing(DataTable table) throws Exception{
        log.info("Creating employee with following \n{}", table);
        navigationSteps.navigateToAddEmployeePage();
        EmployeeForm form = table.convert(EmployeeForm.class, false);
        NewEmployeePage page = new NewEmployeePage(world.driver);
        NewEmployeePage filledPage = (NewEmployeePage) fillEmployeeForm(form, page);
        filledPage.clickCreateButton();
        employeesSteps.validateEmployeeIsNotCreated(form, world.currentCompany.getId());
    }

    private EmployeeBasePage fillEmployeeForm(EmployeeForm form, EmployeeBasePage page) throws Exception {
        page.fillFirstName(form.getFirstName());
        page.fillLastName(form.getLastName());
        page.fillEmailField(form.getEmail());
        page.selectDepartment(form.getDepartmentName());
        page.setAdmin(form.isAdmin());
        page.setAutoApprove(form.isAutoApprove());
        page.fillStartedOn(form.getStartedOn());
        page.fillEndedOn(form.getEndedOn());
        page.fillPassword(form.getPassword());
        page.fillPasswordConfirmation(form.getPasswordConfirmation());
        return page;
    }

    @When("I edit user with email {string} to have:")
    public void iEditUserWithEmailToHave(String email, DataTable table) throws Exception {
        log.info("Editing user with email [{}] with: \r\n{}", email, table);
        EmployeesPage allEmployeesPage = new EmployeesPage(world.driver);
        User userToEdit = userService.getUserWithEmail(email);
        EmployeePage page = allEmployeesPage.navigateToEditEmployeePageForUser(userToEdit.getId());
        EditEmployeeForm form = table.convert(EditEmployeeForm.class, false);
        EmployeePage filledPage = (EmployeePage) fillEmployeeForm(form, page);
        filledPage.clickSaveChangesButton();
        userSteps.validateUserEdited(userToEdit.getId(), form);
        world.editedUserForm = form;
    }

    @When("I delete {string} user from department with name {string}")
    public void iDeleteUserFromDepartmentWithName(String userRole, String departmentName) throws Exception {
        int userToRemoveId;
        Department department = departmentService.getDepartmentWithName(departmentName);
        List<Integer> allUsers = departmentService.getAllUsersExcludingAdmin(department.getId());
        List<Integer> departmentSupervisors = departmentService.getDepartmentSupervisors(department.getId());
        if(StringUtils.equals(userRole,"regular")){
            userToRemoveId = allUsers.stream().filter((u) -> !departmentSupervisors.contains(u)).findFirst().orElse(0);
        } else if (StringUtils.equals(userRole, "supervisor")){
            userToRemoveId = departmentSupervisors.get(0);
        } else if (StringUtils.equals(userRole,"manager")){
          userToRemoveId = departmentService.getBossId(department.getId());
        } else {
            throw new Exception("Role "+ userRole + " is not known");
        }
        EmployeePage employeePage = new EmployeesPage(world.driver).navigateToEditEmployeePageForUser(userToRemoveId);
        employeePage.clickDeleteButton();
        world.deletedUserId = userToRemoveId;
    }
}
