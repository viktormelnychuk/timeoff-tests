package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.NewDepartmentForm;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.Department;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.DepartmentPage;
import com.viktor.timeofftests.pages.DepartmentsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewDepartmentModal;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.DepartmentService;
import com.viktor.timeofftests.services.UserService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
@Log4j2
public class DepartmentStepDefs {

    private World world;
    private CompanyService companyService;
    private DepartmentService departmentService;
    private UserService userService;
    private DepartmentsSteps departmentsSteps;
    public DepartmentStepDefs(World world, CompanyService companyService, DepartmentService departmentService, UserService userService, DepartmentsSteps departmentsSteps){
        this.world = world;
        this.companyService = companyService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.departmentsSteps = departmentsSteps;
    }

    @Given("default department {string} in {string} company is created")
    public void defaultDepartmentInCompanyIsCreated(String departmentName, String companyName) {
        log.info("Creating default department [{}] in company [{}]", departmentName, companyName);
        Company company = companyService.getCompanyWithName(companyName);
        world.currentUserDepartment = departmentService.getOrCreateDepartmentWithName(departmentName, company.getId());
    }
    @Given("following departments are created:")
    public void followingDepartmentsAreCreated(DataTable dataTable) throws Exception {
        log.info("Inserting multiple departments into database");
        int companyId = 0;
        List<NewDepartmentForm> form = dataTable.asList(NewDepartmentForm.class);
        for (NewDepartmentForm item : form) {
            if(StringUtils.isNotEmpty(item.getCompanyName())){
                companyId = companyService.getCompanyWithName(item.getCompanyName()).getId();
            } else {
                companyId = world.currentCompany.getId();
            }
            Department department = departmentService.createDepartment(new Department.Builder()
                    .withName(item.getName())
                    .withAllowance(item.getAllowance())
                    .isAccuredAllowance(item.isAccruedAllowance())
                    .includePublicHolidays(item.isPublicHolidays())
                    .inCompany(companyId)
                    .build());
            userService.createRandomUsersInDepartmentAndCompany(department.getId(),world.currentCompany.getId(),
                    item.getNumberOfUsers());
            if(item.isSecondarySupervisors()){
                departmentService.assignSecondarySupervisors(department.getId(), item.getAmountOfSecondarySupervisors());
            }
            this.world.allDepartments.add(department);
        }
        departmentService.setBossesForAllDepartments(companyId);
        departmentsSteps.validateDepartmentsPresent(world.allDepartments, companyId);
        log.info("Done inserting multiple departments into database for company [{}]",companyId);
    }

    @When("I create following department:")
    public void iCreateFollowingDepartment(DataTable table) throws Exception {
        log.info("Creating department via UI");
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        AddNewDepartmentModal modal = new DepartmentsPage(world.driver).clickAddNewDepartmentButton();
        modal.fillName(data.get("name"));
        modal.selectAllowance(data.get("allowance"));
        if(Objects.equals(data.get("include_pub_holidays"),"true")){
            modal.setIncludePublicHolidays(true);
        } else {
            modal.setIncludePublicHolidays(false);
        }
        if(Objects.equals(data.get("accrued_allowance"),"true")){
            modal.setAccruedAllowance(true);
        } else {
            modal.setAccruedAllowance(false);
        }
        if(StringUtils.isNotEmpty(data.get("boss"))){
            modal.setBoss(getUserIdForEmail(data.get("boss")));
        }
        modal.clickCreateButtonExpectingSuccess();
        departmentsSteps.validateDepartmentWasCreated(data.get("name"),
                data.get("allowance"),
                data.get("include_pub_holidays"),
                data.get("accrued_allowance"));
    }


    private int getUserIdForEmail(String email){
        User  user = world.allUsers.stream()
                .filter((u)->u.getEmail().equals(email))
                .collect(Collectors.toList()).get(0);
        return user.getId();
    }

    @When("I edit {string} department with:")
    public void iEditDepartmentWith(String departmentName, DataTable table) throws Exception {
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        Map<String, String> changed = new HashMap<>();
        Department department = departmentService.getDepartmentWithName(departmentName);
        DepartmentPage page = new DepartmentPage(world.driver);
        if(StringUtils.isNotEmpty(data.get("new_name"))){
            page.fillName(data.get("new_name"));
            changed.put("name", data.get("new_name"));
        }
        if(StringUtils.isNotEmpty(data.get("allowance"))){
            page.setAllowance(Integer.parseInt(data.get("allowance")));
            changed.put("allowance", data.get("allowance"));
        }
        if(StringUtils.isNotEmpty(data.get("include_pub_holidays"))){
            page.setIncludePublicHolidays(Objects.equals(data.get("include_pub_holidays"), "true"));
            changed.put("include_pub_holidays", data.get("include_pub_holidays"));
        }

        if(StringUtils.isNotEmpty(data.get("accrued_allowance"))){
            page.setAccruedAllowance(Objects.equals(data.get("accrued_allowance"),"true"));
            changed.put("accrued_allowance",data.get("accrued_allowance"));
        }
        if(StringUtils.isNotEmpty(data.get("manager"))){
            int userId = userService.getUserWithEmail(data.get("manager")).getId();
            page.selectManger(userId);
            changed.put("manager", String.valueOf(userId));
        }
        page.clickSaveButton();
        departmentsSteps.validateDepartmentChanges(department.getId(), changed);
    }

    @When("I {string} {string} additional supervisors for department {string}")
    public void iAdditionalSupervisor(String action, String amount, String departmentName) throws Exception {
        Department department = departmentService.getDepartmentWithName(departmentName);
        switch (action.toLowerCase()){
            case "add":
                departmentsSteps.addSecondarySupervisors(department.getId(), amount);
                break;
            case "remove":
                departmentsSteps.removeSecondarySupervisors(amount);
                break;
            default:
                throw new Exception("Unknown action ["+action+"]");
        }
    }

    @When("I delete department with name {string}")
    public void iDeleteDepartmentWithName(String departmentName) {
        departmentsSteps.removeDepartment(departmentName);
    }

    @Then("department with name {string} is deleted")
    public void departmentWithNameIsDeleted(String departmentName) {
        departmentsSteps.validateDepartmentIsNotPreset(departmentName);
        departmentsSteps.validateDepartmentsPage();

    }

    @And("department with name {string} is not deleted")
    public void departmentWithNameIsNotDeleted(String departmentName) {
        departmentsSteps.validateDepartmentIsPresent(departmentName);
    }
}
