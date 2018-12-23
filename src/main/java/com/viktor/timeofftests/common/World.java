package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.models.User;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class World {

    public WebDriver driver;
    public Company currentCompany;
    public Company editedCompany;
    public User currentUser;
    public Department currentUserDepartment;
    public Schedule currentCompanySchedule;
}
