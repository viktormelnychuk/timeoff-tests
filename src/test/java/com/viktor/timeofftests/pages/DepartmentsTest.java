package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.partials.modals.AddNewDepartmentModal;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import com.viktor.timeofftests.steps.PreparationSteps;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.viktor.timeofftests.matcher.MapMatchers.mapContainsAllElements;
import static org.hamcrest.Matchers.*;

public class DepartmentsTest extends BaseTest {
    private UserService userService = UserService.getInstance();
    private DepartmentService departmentService = DepartmentService.getInstance();
    private User user;
    private DepartmentsPage departmentsPage;
    @BeforeEach
    void prepare(){
        user = userService.createDefaultAdmin();
        departmentsPage = new DepartmentsPage(getDriver());
    }

    @Test
    void departmentsPageReflectsCorrectInformation(){
        Department defaultDep = departmentService.getDepartmentWithId(user.getDepartmentID());
        List<Department> inserted = PreparationSteps.createVariousDepartments(user.getCompanyID());
        inserted.add(defaultDep);
        departmentsPage = departmentsPage.navigate();
        List<Department> visible = departmentsPage.getDisplayedDepartments();
        assertThat(visible, containsInAnyOrder(inserted.toArray(new Department[inserted.size()])));
    }

    @Test
    void mangerNameIsDisplayedOnTheDepartmentsPage(){
        departmentsPage.navigate();
        Department defaultDep = departmentService.getDepartmentWithId(user.getDepartmentID());
        String displayedName = departmentsPage.getDisplayedManagerNameForDepartment(defaultDep);
        assertThat(displayedName, is(user.getFullName()));
    }

    @Test
    void correctNumberOfEmployeesDisplayed(){
        List<Department> inserted = PreparationSteps.createVariousDepartments(user.getCompanyID());
        inserted.add(departmentService.getDepartmentWithId(user.getDepartmentID()));
        departmentsPage.navigate();
        Map<String, Integer> employeesInDepartment = departmentsPage.getDisplayedEmployeesNumber();
        Map<String, Integer> inDb = departmentService.getAmountOfUsersInDepartments(inserted);
        assertThat(employeesInDepartment, mapContainsAllElements(inDb));
    }

    @Test
    void addNewDepartment(){
        departmentsPage.navigate();
        AddNewDepartmentModal modal = departmentsPage.clickAddNewDepartmentButton();
        departmentsPage = modal.fillName("Name")
                .selectAllowance("20")
                .setIncludePublicHolidays(true)
                .setAccruedAllowance(true)
                .clickCreateButtonExpectingSuccess();
    }
}
