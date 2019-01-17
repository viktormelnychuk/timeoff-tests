package com.viktor.timeofftests.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Leave {
    private int id;
    private LeaveStatus status;
    private String employeeComment;
    private String approverComment;
    private LocalDate decidedAt;
    private LocalDate dateStart;
    private LeaveDayPart dayPartStart;
    private LocalDate dateEnd;
    private LeaveDayPart dayPartEnd;
    private int userId;
    private int approverId;
    private int leaveTypeId;
}
