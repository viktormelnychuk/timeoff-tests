package com.viktor.timeofftests.forms;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeForm {
    private String firstName;
    private String lastName;
    private String email;
    private String departmentName;
    private boolean admin;
    private boolean autoApprove;
    private LocalDate startedOn;
    private LocalDate endedOn;
    private String password;
    private String passwordConfirmation;
}
