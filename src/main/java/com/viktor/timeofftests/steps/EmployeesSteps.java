package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.EmployeeService;
import lombok.extern.log4j.Log4j2;


import java.util.List;

import static com.viktor.timeofftests.matcher.CollectionMatchers.containsAllItems;
import static org.hamcrest.MatcherAssert.assertThat;

@Log4j2
public class EmployeesSteps {
    private World world;
    private EmployeeService employeeService;
    private DepartmentService departmentService;
    public EmployeesSteps(World world, EmployeeService employeeService, DepartmentService departmentService) {
        this.world = world;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    public void validateEmployeesTable() throws Exception {
        log.info("Validating employees table");
        EmployeesPage page = new EmployeesPage(world.driver);
        List<EmployeeRow> displayedEmployees = page.getAllDisplayedEmployees();
        List<EmployeeRow> inDbEmployees = employeeService.getAllEmployees(world.currentCompany.getId());
        assertThat(displayedEmployees, containsAllItems(inDbEmployees));
    }

    public void validateFilteredEmployeesTable(String departmentName) throws Exception {
        log.info("Validating only user from department [{}] are displayed", departmentName);
        EmployeesPage page = new EmployeesPage(world.driver);
        List<EmployeeRow> displayedEmployees= page.getAllDisplayedEmployees();
        Department department = departmentService.getDepartmentWithNameAndCompanyId(departmentName, world.currentCompany.getId());
        List<EmployeeRow> inDb = employeeService.getEmployeesForDepartment(department);
        assertThat(displayedEmployees, containsAllItems(inDb));
    }
}
