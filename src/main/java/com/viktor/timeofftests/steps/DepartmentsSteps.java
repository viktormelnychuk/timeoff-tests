package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.pages.DepartmentPage;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.pages.partials.modals.AddSupervisorsModal;
import com.viktor.timeofftests.services.DepartmentService;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.viktor.timeofftests.matcher.CollectionMatchers.containsAllItems;
import static com.viktor.timeofftests.matcher.CollectionMatchers.hasAllItemsExcludingProperties;
import static com.viktor.timeofftests.matcher.MapMatchers.mapContainsAllElements;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
        assertThat(department.getAllowance(), is(Double.parseDouble(allowance)));
        assertThat(department.isAccuredAllowance(), is(Objects.equals(accrued_allowance,"true")));
        assertThat(department.isIncludePublicHolidays(), is(Objects.equals(include_pub_holidays,"true")));
    }

    public void validateDepartmentChanges(int departmentId, Map<String, String> changed) throws Exception {
        Department department = departmentService.getDepartmentWithId(departmentId);
        for (String s : changed.keySet()) {
            switch (s){
                case "name":
                    assertThat(department.getName(), is(changed.get(s)));
                    break;
                case "allowance":
                    assertThat(department.getAllowance(), is(Double.parseDouble(changed.get("allowance"))));
                    break;
                case "include_pub_holidays":
                    assertThat(department.isIncludePublicHolidays(), is(Objects.equals(changed.get(s),"true")));
                    break;
                case "accrued_allowance":
                    assertThat(department.isAccuredAllowance(), is(Objects.equals(changed.get(s),"true")));
                    break;
                case "manager":
                    assertThat(department.getBossId(), is(Integer.parseInt(changed.get("manager"))));
                    break;
                default:
                    throw new Exception("Unknown property ["+s+"]");
            }
        }
    }

    public void addSecondarySupervisors(int departmentId, String amount) {
        log.info("Adding [{}] secondary supervisors for department [{}]", amount, departmentId);
        List<Integer> allUsersExcludingAdmin = departmentService.getAllUsersExcludingAdmin(departmentId);
        int supervisorsToAdd = Integer.parseInt(amount);
        DepartmentPage page  = new DepartmentPage(world.driver);
        AddSupervisorsModal modal = page.clickAddSecondarySupervisors();
        try {
            List<Integer> usersToAdd = allUsersExcludingAdmin.subList(0, supervisorsToAdd);
            modal.checkUser(usersToAdd);
            modal.clickAddButton();
            validateSecondarySupervisorsAdded(departmentId,usersToAdd);
        } catch (IndexOutOfBoundsException e){
            log.error("Company does not contain enough users");
            throw e;
        }

    }

    private void validateSecondarySupervisorsAdded(int departmentId, List<Integer> usersToAdd) {
        log.info("Validating secondary supervisors were added");
        List<Integer> departmentSupervisors = departmentService.getDepartmentSupervisors(departmentId);
        if(!departmentSupervisors.containsAll(usersToAdd)){
            String builder = "Not all users were found as department supervisors. Expected to have \r\n" +
                    usersToAdd +
                    "\r\n" +
                    "But found " +
                    departmentSupervisors;
            fail(builder);
        }
    }

    public void removeSecondarySupervisors(String amount) {
        log.info("Removing {} secondary supervisors", amount);
        DepartmentPage page = new DepartmentPage(world.driver);
        page.deleteSecondarySupervisor(Integer.parseInt(amount));
    }

    public void validateDepartmentPage(String departmentName) {
        log.info("Validating [] department page", departmentName);
        DepartmentPage page = new DepartmentPage(world.driver);
        Department department = departmentService.getDepartmentWithName(departmentName);
        assertThat(department.getName(), is(page.getDepartmentName()));
        assertThat(department.getBossId(), is(page.getManagerId()));
        assertThat(department.getAllowance(), is(page.getAllowance()));
        assertThat(department.isIncludePublicHolidays(), is(page.isPublicHolidaysIncluded()));
        assertThat(department.isAccuredAllowance(), is(page.isAccruedAllowance()));

        List<Integer> displayedSecondaryApprovers = page.getSecondaryApproversIds();
        List<Integer> departmentSupervisors = departmentService.getDepartmentSupervisors(department.getId());
        assertThat(displayedSecondaryApprovers, containsAllItems(departmentSupervisors));
    }

    public void removeDepartment(String departmentName) {
        log.info("Removing department with name {}", departmentName);
        DepartmentsPage departmentsPage = new DepartmentsPage(world.driver);
        DepartmentPage departmentPage = departmentsPage.clickDepartmentLink(departmentName);
        departmentPage.clickDeleteButton();
    }

    public void validateDepartmentIsNotPreset(String departmentName) {
        log.info("Validating department [{}] does not exist in database", departmentName);
        Department department = departmentService.getDepartmentWithName(departmentName);
        assertThat(department, nullValue());
    }

    public void validateDepartmentIsPresent(String departmentName) {
        log.info("Validating department [{}] exists in database", departmentName);
        Department department = departmentService.getDepartmentWithName(departmentName);
        assertThat(department, notNullValue());
    }
}
