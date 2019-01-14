package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.EmployeeRow;
import com.viktor.timeofftests.pages.EmployeesPage;

import java.util.List;

public class EmployeesSteps {
    private World world;

    public EmployeesSteps(World world) {
        this.world = world;
    }

    public void validateEmployeesTable() {
        EmployeesPage page = new EmployeesPage(world.driver);
        List<EmployeeRow> allEmployees = page.getAllEmployees();
        
    }
}
