package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.DateUtil;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.InsertLeaveForm;
import com.viktor.timeofftests.models.*;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.LeaveService;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class LeaveStepDefs {
    private UserService userService;
    private World world;
    private LeaveTypeService leaveTypeService;
    private DepartmentService departmentService;
    private LeaveService leaveService;

    public LeaveStepDefs(UserService userService, World world, LeaveTypeService leaveTypeService, DepartmentService departmentService, LeaveService leaveService) {
        this.userService = userService;
        this.world = world;
        this.leaveTypeService = leaveTypeService;
        this.departmentService = departmentService;
        this.leaveService = leaveService;
    }

    @Given("following leaves are created:")
    public void followingLeavesAreCreated(DataTable dataTable) {
        List<InsertLeaveForm> list = dataTable.asList(InsertLeaveForm.class);
        for (InsertLeaveForm form : list) {
            Leave leave = new Leave();
            User user = userService.getUserWithEmail(form.getUserEmail());
            leave.setUserId(user.getId());
            Department department = departmentService.getDepartmentWithId(user.getDepartmentID());
            if(StringUtils.isEmpty(form.getApprover())){
                leave.setApproverId(departmentService.getBossId(department.getId()));
            } else {
                User approver = userService.getUserWithEmail(form.getApprover());
                leave.setApproverId(approver.getId());
            }
            leave.setDayPartStart(LeaveDayPart.ALL);
            leave.setDayPartEnd(LeaveDayPart.ALL);
            LocalDate startDate = DateUtil.selectStartDay();
            LocalDate endDate = DateUtil.selectEndDay(startDate, form.getAmountOfDays());
            leave.setDateStart(startDate);
            leave.setDateEnd(endDate);
            if(StringUtils.isNotEmpty(form.getStatus())){
                leave.setStatus(LeaveStatus.valueOf(form.getStatus().toUpperCase()));
            } else {
                leave.setStatus(LeaveStatus.APPROVED);
            }
            LeaveType leaveType = leaveTypeService.getLeaveTypeByNameAndCompanyId(form.getLeaveName(),
                    world.currentCompany.getId());
            leave.setLeaveTypeId(leaveType.getId());
            leave.setDecidedAt(form.getDecidedAt());
            Leave inserted = leaveService.insertLeave(leave);
        }

    }
}
