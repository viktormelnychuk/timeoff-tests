package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.forms.NewEmployeeForm;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.pages.NewEmployeePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class EmployeesStepDefs {

    private World world;
    private EmployeesSteps employeesSteps;
    private NavigationSteps navigationSteps;

    public EmployeesStepDefs(World world, EmployeesSteps employeesSteps, NavigationSteps navigationSteps) {
        this.world = world;
        this.employeesSteps = employeesSteps;
        this.navigationSteps = navigationSteps;
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
        NewEmployeeForm form = table.convert(NewEmployeeForm.class, false);
        fillNewEmployeeForm(form);
        employeesSteps.validateEmployeeCreated(form, world.currentCompany.getId());
    }

    @When("I try to create an employee with following:")
    public void iTryToCreateAnEmployeeWithFollowing(DataTable table) throws Exception{
        log.info("Creating employee with following \n{}", table);
        NewEmployeeForm form = table.convert(NewEmployeeForm.class, false);
        fillNewEmployeeForm(form);
        employeesSteps.validateEmployeeIsNotCreated(form, world.currentCompany.getId());
    }

    private void fillNewEmployeeForm (NewEmployeeForm form) throws Exception {
        navigationSteps.navigateToAddEmployeePage();
        NewEmployeePage page = new NewEmployeePage(world.driver);
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
        page.clickCreateButton();
    }
}
