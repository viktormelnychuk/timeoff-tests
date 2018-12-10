package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DepartmentsTest extends BaseTest {
    private UserService userService = UserService.getInstance();
    private DepartmentService departmentService = DepartmentService.getInstance();
    private User user;
    private DepartmentsPage departmentsPage;
    @BeforeEach
    void prepare(){
        user = userService.createDefaultAdmin();
        departmentsPage = new DepartmentsPage(getDriver());
        departmentsPage.navigate();
    }

    @Test
    void departmentsPageReflectsCorrectInformation(){
        Department defaultDep = departmentService.getDepartmentWithId(user.getDepartmentID());
        Department withPublicHolidays = new Department.Builder()
                .withName("With Public Holidays")
                .withAllowance(1)
                .includePublicHolidays(true)
                .inCompany("Acme")
                .isAccuredAllowance(false)
                .build();
        Department withAllowance = new Department.Builder()
                .withAllowance(15)
                .withName("With accured allowance")
                .includePublicHolidays(false)
                .isAccuredAllowance(true)
                .inCompany("Acme")
                .build();
        Department withAllowanceAndHolidays = new Department.Builder()
                .withAllowance(15)
                .withName("With accured allowance and holidays")
                .includePublicHolidays(true)
                .isAccuredAllowance(true)
                .inCompany("Acme")
                .build();
        List<Department> insertedDeps = departmentService.insertDepartments(
                withAllowance,withAllowanceAndHolidays,withPublicHolidays);
        insertedDeps.add(defaultDep);
        User secondUser = userService.createNewUser(new User.Builder()
                .withEmail("tester2@viktor.com")
                .withName("Jane")
                .withLastName("Doe")
                .inDepartment(withPublicHolidays)
                .inCompany("Acme")
                .build());
        User thirdUser = userService.createNewUser(new User.Builder()
                .withEmail("tester3@viktor.com")
                .withName("James")
                .withLastName("Doe")
                .inDepartment(withAllowance)
                .inCompany("Acme")
                .build());
        User fourthUser = userService.createNewUser(new User.Builder()
                .withEmail("tester4@viktor.com")
                .withName("Janna")
                .withLastName("Doe")
                .inDepartment(withAllowance)
                .inCompany("Acme")
                .build());
        userService.makeDepartmentAdmin(secondUser);
        userService.makeDepartmentAdmin(thirdUser);;
        departmentsPage = departmentsPage.reload();
    }
}
