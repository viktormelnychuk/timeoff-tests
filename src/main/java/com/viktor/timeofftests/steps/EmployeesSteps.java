package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.LeaveService;
import com.viktor.timeofftests.services.UserService;

import java.util.List;

public class EmployeesSteps {
    private World world;
    private UserService userService;
    private LeaveService leaveService;
    public EmployeesSteps(World world, UserService userService, LeaveService leaveService) {
        this.world = world;
        this.userService = userService;
        this.leaveService = leaveService;
    }

    public void validateEmployeesTable() {
        EmployeesPage page = new EmployeesPage(world.driver);
        List<EmployeeRow> displayedEmployees = page.getAllEmployees();
        List<User> allUsersInCompany = userService.getAllUsersInCompany(world.currentCompany.getId());
        for (User user : allUsersInCompany) {
            int i = leaveService.amountOfUsedDays(user.getId());
            System.out.println(i);
        }
    }
}
