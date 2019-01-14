package com.viktor.timeofftests.models;

import lombok.Data;

import java.util.Date;

@Data
public class Leave {
    private int id;
    private LeaveStatus status;
    private String employeeComment;
    private String approverComment;
    private Date decidedAt;
    private Date dateStart;
    private LeaveDayPart dayPartStart;
    private Date dateEnd;
    private LeaveDayPart dayPartEnd;
    private int userId;
    private int approverId;
    private int leaveTypeId;
}
