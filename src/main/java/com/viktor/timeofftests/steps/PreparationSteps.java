package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.UserPool;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class PreparationSteps {
    private static DepartmentService departmentService = DepartmentService.getInstance();
    private static UserService userService = UserService.getInstance();
    public static List<Department> createVariousDepartments(int companyId){
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
        departmentService.insertDepartments(withAllowance,withAllowanceAndHolidays,withPublicHolidays);
        insertUsersForDepartment(withPublicHolidays, true);
        insertUsersForDepartment(withPublicHolidays, false);
        insertUsersForDepartment(withAllowance, true);
        List<Department> departments = new ArrayList<>();
        departments.add(withAllowance);
        departments.add(withPublicHolidays);
        departments.add(withAllowanceAndHolidays);
        return departments;

    }

    public static void insertUsersForDepartment(Department department, boolean isAdmin){
        User user = userService.createNewUser(new User.Builder()
                .withEmail(UserPool.getEmail())
                .withName(UserPool.getName())
                .withLastName(UserPool.getLastName())
                .inDepartment(department)
                .inCompany(department.getCompanyId())
                .build());

        if(isAdmin){
            userService.makeDepartmentAdmin(user);
            department.setBossId(user.getId());
        }
    }

}
