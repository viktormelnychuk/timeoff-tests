package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.services.DepartmentService;

import java.util.List;

public class DepartmentsSteps {
    private DepartmentService departmentService;

    public DepartmentsSteps(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void validateDepartmentsPresent(List<Department> allDepartments, int companyId) throws Exception {
        boolean fail = false;
        StringBuilder result = new StringBuilder();
        List<Department> departmentsInDb = departmentService.getAllDepartmentsForCompany(companyId);
        for (Department department : allDepartments) {
            if(!departmentsInDb.contains(department)){
                fail = true;
                result.append(String.format("department with name <%s> and id <%d> was not found in database \n\r", department.getName(), department.getId()));
            }
        }
        if(fail){
            throw new Exception(result.toString());
        }
    }
}
