package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class SignupForm {
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordConfirmation;
    private String password;
    private String timezone;
    private String country;
}
