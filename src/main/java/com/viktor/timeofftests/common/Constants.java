package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.LeaveType;

public class Constants {
    public static final String DEFAULT_USER_EMAIL = "tester@viktor.com";
    public static final String DEFAULT_USER_PASSWORD = "1234";
    public static final String DEFAULT_USER_NAME = "John";
    public static final String DEFAULT_USER_LAST_NAME = "Doe";
    public static final String DEFAULT_COMPANY_NAME = "Acme";
    public static final String DEFAULT_COMPANY_COUNTRY = "US";
    public static final String DEFAULT_COMPANY_TIMEZONE = "US/Central";
    public static final String PASSWORD_HASH_SECRET = "!2~`HswpPPLa22+=±§sdq qwe,appp qwwokDF_";
    public static final String DEFAULT_DEPARTMENT_NAME = "SALES";
    public static final int DEFAULT_DEPARTMENT_ALLOWANCE = 20;
    public static final String COOKIE_SIGNING_SECRET = "my dirty secret ;khjsdkjahsdajhasdam,nnsnad,";
    public static final LeaveType[] DEFAULT_LEAVE_TYPES = {
        new LeaveType("Holiday", "leave_type_color_1", true, 0, 0, 0),
        new LeaveType("Sick Leave", "leave_type_color_3", false, 10, 0, 0)
    };
}
