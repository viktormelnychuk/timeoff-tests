package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.LeaveLeaveType;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.services.AllowanceService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.LeaveService;
import com.viktor.timeofftests.services.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CalendarSteps {
    private AllowanceService allowanceService;
    private UserService userService;
    private DepartmentService departmentService;
    private LeaveService leaveService;
    private World world;
    public CalendarSteps(AllowanceService allowanceService, UserService userService, DepartmentService departmentService, LeaveService leaveService, World world){
        this.allowanceService = allowanceService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.leaveService = leaveService;
        this.world = world;
    }

    public void validateCalendarPage(User user) throws Exception {
        CalendarPage calendarPage = new CalendarPage(world.driver);
        Department department = departmentService.getDepartmentWithId(user.getDepartmentID());
        int availableAllowance = allowanceService.getAvailableAllowance(user);
        int usedDays = allowanceService.amountOfUsedDays(user.getId());
        List<LeaveLeaveType> allLeaves = leaveService.getAllLeavesForUserInThisYear(user.getId());
        assertThat(calendarPage.getTitle(), is(TextConstants.CalendarPageConstants.PAGE_TITLE));
        String expectedGreeting = String.format(TextConstants.CalendarPageConstants.EMPLOYEE_GREETING_F,user.getName(), user.getLastName());
        assertThat(calendarPage.getEmployeeGreeting(), is(expectedGreeting));
    }
}
