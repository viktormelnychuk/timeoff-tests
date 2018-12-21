package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import org.openqa.selenium.WebDriver;

public class World {

    public WebDriver driver;
    public Company company;
    public User defaultUser;
    public Department currentUserDepartment;
}
