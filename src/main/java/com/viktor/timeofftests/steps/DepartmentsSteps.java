package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.services.DepartmentService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.viktor.timeofftests.matcher.CollectionMatchers.hasAllItemsExcludingProperties;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DepartmentsSteps {
    private DepartmentService departmentService;
    private World world;

    public DepartmentsSteps(DepartmentService departmentService, World world) {
        this.departmentService = departmentService;
        this.world = world;
    }

    public void validateDepartmentsPresent(List<Department> allDepartments, int companyId) throws Exception {
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

    public void validateDisplayedDepartments(int id) {
        DepartmentsPage page = new DepartmentsPage(world.driver);
        List<Department> displayed = page.deserializeDepartments(world.currentCompany.getId());
        List<Department> inDb = departmentService.getAllDepartmentsForCompany(world.currentCompany.getId());
        inDb.sort(Comparator.comparing(Department::getName));
        assertThat(displayed, hasAllItemsExcludingProperties(inDb));
    }

    public void validateDepartmentWasCreated(String name, String allowance, String include_pub_holidays, String accrued_allowance){
        boolean fail = false;
        StringBuilder result = new StringBuilder();
        Department department = departmentService.getDepartmentWithNameAndCompanyId(name, world.currentCompany.getId());
        assertThat(department.getAllowance(), is(Integer.parseInt(allowance)));
        assertThat(department.isAccuredAllowance(), is(Objects.equals(accrued_allowance,"true")));
        assertThat(department.isIncludePublicHolidays(), is(Objects.equals(include_pub_holidays,"true")));
    }
}
