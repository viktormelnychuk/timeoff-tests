package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.EmployeeService;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class EmployeesStepDefs {

    private World world;
    private EmployeesSteps employeesSteps;

    public EmployeesStepDefs(World world, EmployeesSteps employeesSteps) {
        this.world = world;
        this.employeesSteps = employeesSteps;
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
}
