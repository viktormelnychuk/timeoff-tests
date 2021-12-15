package com.viktor.timeofftests.forms;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsertLeaveForm {
    private String userEmail;
    private String leaveName;
    private int amountOfDays;
    private String status;
    private String employeeComment;
    private String approverComment;
    private String approver;
    private LocalDate decidedAt;
}
