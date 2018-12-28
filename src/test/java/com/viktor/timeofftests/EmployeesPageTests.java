//package com.viktor.timeofftests.pages;
//
//import com.viktor.timeofftests.models.Department;
//import com.viktor.timeofftests.models.User;
//import com.viktor.timeofftests.services.DepartmentService;
//import com.viktor.timeofftests.services.UserService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class EmployeesPageTests extends BaseTest {
//    User adminUser;
//    List<User> usersList = new ArrayList<>();
//    List<Department> departmentList;
//    private UserService userService = UserService.getInstance();
//    private DepartmentService departmentService = DepartmentService.getInstance();
//    @BeforeEach
//    void setup(){
//        adminUser = userService.createDefaultAdmin();
//        for (int i = 0; i < 15; i++){
//            usersList.add(userService.createRandomUsersInDifferentDepartments());
//        }
//        departmentList = departmentService.getAllDepartmentsForCompany(adminUser.getCompanyID());
//        departmentService.setBossesForAllDepartments(adminUser.getCompanyID());
//    }
//
//    @Test
//    void allEmployeesDisplayed(){
//    }
//}
