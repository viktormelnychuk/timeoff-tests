package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.services.DepartmentService;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.viktor.timeofftests.matcher.CollectionMatchers.hasAllItemsExcludingProperties;
import static com.viktor.timeofftests.matcher.MapMatchers.mapContainsAllElements;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
@Log4j2
public class DepartmentsSteps {
    private DepartmentService departmentService;
    private World world;

    public DepartmentsSteps(DepartmentService departmentService, World world) {
        this.departmentService = departmentService;
        this.world = world;
    }

    public void validateDepartmentsPresent(List<Department> allDepartments, int companyId) throws Exception {
        log.info("Verifying departments are present on the page");
        boolean fail = false;
        StringBuilder result = new StringBuilder();
        List<Department> departmentsInDb = departmentService.getAllDepartmentsForCompany(companyId);
        for (Department department : allDepartments) {
            if(!departmentsInDb.contains(department)){
                fail = true;
                result.append(String.format("department with name <%s> and id <%d> was not found in database \n\r", department.getName(), department.getId()));
            }
        }
        if(fail){
            throw new Exception(result.toString());
        }
    }

    public void validateDepartmentsPage() {
        log.info("Verifying departments page");
        log.info("Verifying displayed departments properties match database");
        DepartmentsPage page = new DepartmentsPage(world.driver);
        List<Department> displayed = page.deserializeDepartments(world.currentCompany.getId());
        List<Department> inDb = departmentService.getAllDepartmentsForCompany(world.currentCompany.getId());
        inDb.sort(Comparator.comparing(Department::getName));
        assertThat(displayed, hasAllItemsExcludingProperties(inDb));
        log.info("Verifying number of employees match expected");
        // validate number of employees
        Map<String, Integer> employeesInDb = departmentService.getAmountOfUsersInDepartments(inDb);
        Map<String, Integer> displayedNumOfEmployees = page.getDisplayedEmployeesNumber();
        assertThat(displayedNumOfEmployees, mapContainsAllElements(employeesInDb));
        log.info("Verifying manager names");
        // validate manager name
        Map<String, String> displayedManagers = page.getDisplayedManagers();
        Map<String, String> inDbManagers = departmentService.getManagersForDepartments(inDb);
        assertThat(displayedManagers, mapContainsAllElements(inDbManagers));
    }

    public void validateDepartmentWasCreated(String name, String allowance, String include_pub_holidays, String accrued_allowance){
        log.info("Verifying department with name [{}] was created", name);
        Department department = departmentService.getDepartmentWithNameAndCompanyId(name, world.currentCompany.getId());
        assertThat(department.getAllowance(), is(Integer.parseInt(allowance)));
        assertThat(department.isAccuredAllowance(), is(Objects.equals(accrued_allowance,"true")));
        assertThat(department.isIncludePublicHolidays(), is(Objects.equals(include_pub_holidays,"true")));
    }
}
