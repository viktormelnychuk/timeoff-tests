package com.viktor.timeofftests.services;

import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {
    private UserService userService;
    private AllowanceService allowanceService;

    public EmployeeService(UserService userService, AllowanceService allowanceService) {
        this.userService = userService;
        this.allowanceService = allowanceService;
    }

    public List<EmployeeRow> getEmployeesForDepartment(Department department) throws Exception {
        List<EmployeeRow> allEmployees = getAllEmployees(department.getCompanyId());
        return allEmployees.stream()
                .filter((e)-> e.getDepartmentId() == department.getId())
                .collect(Collectors.toList());
    }

    public List<EmployeeRow> getAllEmployees (int companyId) throws Exception {
        List<EmployeeRow> result = new ArrayList<>();
        List<User> allUsersInCompany = userService.getAllUsersInCompany(companyId);
        for (User user : allUsersInCompany) {
            int daysUsed = allowanceService.amountOfUsedDays(user.getId());
            EmployeeRow row = new EmployeeRow();
            row.setName(user.getFullName());
            row.setAdmin(user.isAdmin());
            row.setDaysUsed(daysUsed);
            row.setDepartmentId(user.getDepartmentID());
            int totalAvailableAllowance = allowanceService.getAvailableAllowance(user);
            row.setAvailableAllowance(totalAvailableAllowance - daysUsed);
            result.add(row);
        }
        return result;
    }
}
