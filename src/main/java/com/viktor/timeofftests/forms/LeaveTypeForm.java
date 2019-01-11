package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class LeaveTypeForm {
    private String name;
    private boolean primary;
    private String color;
    private boolean useAllowance;
    private int limit;
}
