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
        User firstUser = userService.createRandomUser();
        User secondUser = userService.createRandomUser();
        User thirdUser = userService.createRandomUser();
        User fourthUser = userService.createRandomUser();
        departmentsPage.navigate();
        AddNewDepartmentModal addNewDepartmentModal = departmentsPage.clickAddNewDepartmentButton();
        departmentsPage = addNewDepartmentModal.fillName("Department1")
                .selectAllowance("20")
                .setIncludePublicHolidays(true)
                .setAccruedAllowance(true)
                .setBoss(firstUser.getId())
                .clickCreateButtonExpectingSuccess();
        departmentsPage.clickAddNewDepartmentButton();
        departmentsPage = addNewDepartmentModal
                .fillName("Department2")
                .selectAllowance("30")
                .setIncludePublicHolidays(false)
                .setAccruedAllowance(true)
                .setBoss(secondUser.getId())
                .clickCreateButtonExpectingSuccess();
        departmentsPage.clickAddNewDepartmentButton();
        departmentsPage = addNewDepartmentModal
                .fillName("Department3")
                .selectAllowance("2")
                .setIncludePublicHolidays(true)
                .setAccruedAllowance(false)
                .setBoss(thirdUser.getId())
                .clickCreateButtonExpectingSuccess();
        departmentsPage.clickAddNewDepartmentButton();
        departmentsPage = addNewDepartmentModal
                .fillName("Department3")
                .selectAllowance("0")
                .setIncludePublicHolidays(true)
                .setAccruedAllowance(false)
                .setBoss(fourthUser.getId())
                .clickCreateButtonExpectingSuccess();
        List<Department> visible = departmentsPage.getDisplayedDepartments();
        List<Department> inDb = departmentService.getAllDepartmentsForCompany(user.getCompanyID());
        assertThat(visible, containsInAnyOrder(inDb.toArray(new Department[inDb.size()])));
    }

    @Test
    void nameRequiredWhenCreatingDepartment(){
        departmentsPage.navigate();
        AddNewDepartmentModal modal = departmentsPage.clickAddNewDepartmentButton();
        modal = modal.clickCreateButtonExpectingFailure();
        assertThat(modal.modalDisplayed(), describedAs("modal displayed",is(true)));
    }

    @Test
    void checkDefaultModalState(){
        departmentsPage.navigate();
        AddNewDepartmentModal modal = departmentsPage.clickAddNewDepartmentButton();
        assertAll(
                ()-> assertThat(modal.publicHolidaysChecked(), describedAs("public holidays checked",is(true))),
                ()-> assertThat(modal.accruedChecked(), describedAs("accrued checked",is(false)))
        );
    }
}
