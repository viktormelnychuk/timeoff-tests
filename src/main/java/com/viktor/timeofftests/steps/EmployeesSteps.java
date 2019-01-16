package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.EmployeesPage;
import com.viktor.timeofftests.services.AllowanceService;
import com.viktor.timeofftests.services.LeaveService;
import com.viktor.timeofftests.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static com.viktor.timeofftests.matcher.CollectionMatchers.containsAllItems;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmployeesSteps {
    private World world;
    private UserService userService;
    private LeaveService leaveService;
    private AllowanceService allowanceService;
    public EmployeesSteps(World world, UserService userService, LeaveService leaveService, AllowanceService allowanceService) {
        this.world = world;
        this.userService = userService;
        this.leaveService = leaveService;
        this.allowanceService = allowanceService;
    }

    public void validateEmployeesTable() throws Exception {
        EmployeesPage page = new EmployeesPage(world.driver);
        world.driver.navigate().refresh();
        List<EmployeeRow> displayedEmployees = page.getAllEmployees();
        List<EmployeeRow> inDbEmployees = new ArrayList<>();
        List<User> allUsersInCompany = userService.getAllUsersInCompany(world.currentCompany.getId());
        for (User user : allUsersInCompany) {
            int daysUsed = allowanceService.amountOfUsedDays(user.getId());
            EmployeeRow row = new EmployeeRow();
            row.setName(user.getFullName());
            row.setAdmin(user.isAdmin());
            row.setDaysUsed(daysUsed);
            row.setDepartmentId(user.getDepartmentID());
            int totalAvailableAllowance = allowanceService.getAvailableAllowance(user);
            row.setAvailableAllowance(totalAvailableAllowance - daysUsed);
            inDbEmployees.add(row);
        }

        assertThat(displayedEmployees, containsAllItems(inDbEmployees));
    }
}
