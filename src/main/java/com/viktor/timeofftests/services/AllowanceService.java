package com.viktor.timeofftests.services;

import com.viktor.timeofftests.models.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

public class AllowanceService {

    private LeaveService leaveService;
    private ScheduleService scheduleService;
    private BankHolidaysService bankHolidaysService;
    private DepartmentService departmentService;

    public AllowanceService(LeaveService leaveService, ScheduleService scheduleService, BankHolidaysService bankHolidaysService, DepartmentService departmentService) {
        this.leaveService = leaveService;
        this.scheduleService = scheduleService;
        this.bankHolidaysService = bankHolidaysService;
        this.departmentService = departmentService;
    }

    public int amountOfUsedDays (int userId) throws Exception {
        int result = 0;
        List<LeaveLeaveType> allLeavesForUser = leaveService.getAllLeavesForUserInThisYear(userId);
        for (LeaveLeaveType leaveLeaveType : allLeavesForUser) {
            Leave leave = leaveLeaveType.getLeave();
            LeaveType leaveType = leaveLeaveType.getLeaveType();
            if(!leaveType.isUseAllowance()){ continue; }
            LocalDate dateStart = leave.getDateStart();
            LocalDate dateEnd = leave.getDateEnd();
            Schedule userSchedule = scheduleService.getScheduleForUserId(userId);
            for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date=date.plusDays(1)){
                if(userSchedule.isWorkingDay(date) && !bankHolidaysService.isHoliday(date, userId)){
                    result++;
                }
            }
        }
        return result;
    }

    public int getAvailableAllowance(User user) {
        /*
                 available allowances are calculated by formula:
                 -1 * [department.allowance] - Math.round([department.allowance] * [amount of days in current year]/365)
        */
        double nominalAllowance = departmentService.getDepartmentWithId(user.getDepartmentID()).getAllowance();
        // find start day
        LocalDate userStartDate = user.getStartDate();
        LocalDate userEndDate;
        if(user.getEndDate() == null){
            userEndDate = LocalDate.now().with(lastDayOfYear());
        } else {
            userEndDate = user.getEndDate();
            if(userEndDate.getYear() != LocalDate.now().getYear()){
                userEndDate = LocalDate.now().with(lastDayOfYear());
            }
        }
        double daysInThisYear = ChronoUnit.DAYS.between(userStartDate, userEndDate);
        double notUsed = nominalAllowance - Math.round(nominalAllowance * (daysInThisYear/365));
        return (int)(nominalAllowance - notUsed);
    }
}
