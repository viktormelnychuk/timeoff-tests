package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class NewDepartmentForm {
    private String name;
    private int allowance;
    private boolean publicHolidays;
    private boolean accruedAllowance;
    private int numberOfUsers;
    private String companyName;
}
