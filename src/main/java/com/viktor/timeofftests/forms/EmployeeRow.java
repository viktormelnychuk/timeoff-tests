package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class EmployeeRow {
    private String name;
    private int departmentId;
    private boolean admin;
    private int availableAllowance;
    private int daysUsed;
}
