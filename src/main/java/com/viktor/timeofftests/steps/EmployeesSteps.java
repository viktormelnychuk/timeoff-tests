package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.UserService;

import java.util.List;

public class EmployeesSteps {
    private World world;
    private UserService userService;
    public EmployeesSteps(World world, UserService userService) {
        this.world = world;
        this.userService = userService;
    }

    public void validateEmployeesTable() {
        EmployeesPage page = new EmployeesPage(world.driver);
        List<EmployeeRow> displayedEmployees = page.getAllEmployees();
        List<User> allUsersInCompany = userService.getAllUsersInCompany(world.currentCompany.getId());

    }
}
