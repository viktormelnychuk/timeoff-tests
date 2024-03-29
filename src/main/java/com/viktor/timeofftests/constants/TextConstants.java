package com.viktor.timeofftests.constants;

public class TextConstants {
    public class CalendarPageConstants{
        public static final String PAGE_TITLE = "Employee calendar";
        public static final String PAGE_URL = "http://localhost:3000/calendar/";
        public static final String EMPLOYEE_GREETING_F = "%s %s's calendar for 2019";
    }

    public class LoginPageConstants {
        public static final String PAGE_URL = "http://localhost:3000/login";
        public static final String ERROR_MESSAGE = "Incorrect credentials";
    }

    public class RegisterPageConstants {
        public static final String PAGE_URL = "http://localhost:3000/register";
    }

    public class GeneralSettingsConstants{
        public static final String PAGE_URL = "http://localhost:3000/settings/general/";
    }

    public class DepartmentsConstants {
        public static final String PAGE_URL = "http://localhost:3000/settings/departments/";
    }

    public class EmployeesPageConstants {
        public static final String ALL_DEPARTMENTS_NAME = "All departments";
        public static final String PAGE_URL = "http://localhost:3000/users/";
    }

    public class AddNewEmployeePage {
        public static final String PAGE_URL = "http://localhost:3000/users/add/";
    }
}
