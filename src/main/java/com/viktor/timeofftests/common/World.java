package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.*;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World {

    public WebDriver driver;
    public Company currentCompany;
    public Company editedCompany;
    public User currentUser;
    public Department currentUserDepartment;
    public Schedule currentCompanySchedule;
    public List<LeaveType> inDbLeaveTypes;
    public List<Department> allDepartments = new ArrayList<>();
}
