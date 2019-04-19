package com.viktor.timeofftests.common;

import com.viktor.timeofftests.forms.EmployeeForm;
import com.viktor.timeofftests.models.*;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class World {

    public WebDriver driver;
    public Company currentCompany;
    public Company editedCompany;
    public User currentUser;
    public Department currentUserDepartment;
    public List<Department> allDepartments = new ArrayList<>();
    public List<User> allUsers = new ArrayList<>();
    public EmployeeForm editedUserForm;
    public int deletedUserId;
}
