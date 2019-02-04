package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.forms.EmployeeForm;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.EmployeeService;
import com.viktor.timeofftests.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.viktor.timeofftests.matcher.CollectionMatchers.containsAllItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Log4j2
public class EmployeesSteps {
    private World world;
    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private UserService userService;
    public EmployeesSteps(World world, EmployeeService employeeService, DepartmentService departmentService, UserService userService) {
        this.world = world;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.userService = userService;
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

    public void validateEmployeeCreated(EmployeeForm form, int companyId) {
        List<User> allUsersInCompany = userService.getAllUsersInCompany(companyId);
        Optional<User> optionalUser = allUsersInCompany.stream().filter((u)-> StringUtils.equals(u.getEmail(),form.getEmail())).findFirst();
        assertThat(optionalUser.isPresent(), is(true));
        User createdUser = optionalUser.get();
        assertThat(createdUser.getName(), is(form.getFirstName()));
        assertThat(createdUser.getLastName(), is(form.getLastName()));
        Department userDepartment = departmentService.getDepartmentWithId(createdUser.getDepartmentID());
        assertThat(userDepartment.getName(), is(form.getDepartmentName()));
        assertThat(createdUser.isAdmin(), is(form.isAdmin()));
        assertThat(createdUser.isAutoApprove(), is(form.isAutoApprove()));
        // TODO: Uncomment after migrating to LocalDate

        //  assertThat(createdUser.getStartDate(), is(form.getStartedOn()));
    }

    public void validateEmployeeIsNotCreated(EmployeeForm form, int companyId) {
        List<User> allUsersInCompany = userService.getAllUsersInCompany(companyId);
        User createdUser = allUsersInCompany.stream().filter((u)-> StringUtils.equals(u.getEmail(),form.getEmail()))
                .findFirst()
                .orElse(null);
        if(!Objects.isNull(createdUser)){
            assertThat(createdUser.getName(), is(not(form.getFirstName())));
            assertThat(createdUser.getLastName(), is(not(form.getLastName())));

        }

    }
}
