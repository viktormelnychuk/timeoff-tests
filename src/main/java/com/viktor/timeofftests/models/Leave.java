package com.viktor.timeofftests.models;

import lombok.Data;

import java.util.Date;

@Data
public class Leave {
    private int id;
    private int status;
    private String employeeComment;
    private String approverComment;
    private Date decidedAt;
    private Date dateStart;
    private int dayPartStart;
    private Date dateEnd;
    private int dayPartEnd;
    private int userId;
    private int approverId;
    private int leaveTypeId;
}
