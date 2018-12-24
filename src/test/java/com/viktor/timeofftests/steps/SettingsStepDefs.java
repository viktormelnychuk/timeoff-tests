package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.CompanySettingsForm;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.ScheduleService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class SettingsStepDefs {

    private World world;
    private ScheduleService scheduleService;
    private LeaveTypeService leaveTypeService;
    public SettingsStepDefs(World world, ScheduleService scheduleService, LeaveTypeService leaveTypeService){
        this.world = world;
        this.scheduleService = scheduleService;
        this.leaveTypeService = leaveTypeService;
    }


    @When("I edit company settings with following:")
    public void iEditCompanySettingsWithFollowing(DataTable dataTable) {
        world.editedCompany = world.currentCompany;
        CompanySettings companySettings = new CompanySettings(world.driver);
        CompanySettingsForm form = dataTable.convert(CompanySettingsForm.class, false);
        if(StringUtils.isNotEmpty(form.getCompanyName())){
            companySettings.setCompanyName(form.getCompanyName());
            world.editedCompany.setName(form.getCompanyName());
        }
        if(StringUtils.isNotEmpty(form.getCountry())){
            companySettings.setCompanyCountry(form.getCountry());
            world.editedCompany.setCountry(form.getCountry());
        }
        if(StringUtils.isNotEmpty(form.getDateFormat())){
            companySettings.setCompanyDateFormat(form.getDateFormat());
            world.editedCompany.setDateFormat(form.getDateFormat());
        }
        if(StringUtils.isNotEmpty(form.getTimezone())){
            companySettings.setCompanyTimeZone(form.getTimezone());
            world.editedCompany.setTimezone(form.getTimezone());
        }
        companySettings.saveCompanySettings();
    }

    @When("I edit weekly schedule to:")
    public void iEditWeeklyScheduleTo(DataTable table) {
        Map<String, String> map = table.transpose().asMap(String.class, String.class);
        CompanyScheduleSettings page = new CompanyScheduleSettings(world.driver);
        for (String s : map.keySet()) {
            page.setDay(s, transofrmTrueAndFalseToBool(map.get(s)));
        }
        page.saveSchedule();
    }

    private boolean transofrmTrueAndFalseToBool(String s){
        return Objects.equals(s, "true");
    }

    @When("I edit {string} leave type to:")
    public void iEditLeaveTypeTo(String toEdit, DataTable table) {
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        LeaveTypesSettings page = new LeaveTypesSettings(world.driver);
        if(StringUtils.isNotEmpty(data.get("name"))){
            page.editLeaveTypeName(toEdit, data.get("name"));
        }

        if(StringUtils.isNotEmpty(data.get("primary"))){
            page.setPrimaryLeaveType(toEdit);
        }

        if(StringUtils.isNotEmpty(data.get("color"))){
            page.setColor(toEdit, StringUtils.capitalize(data.get("color")));
        }

        if(StringUtils.isNotEmpty(data.get("use_allowance"))){
            if(data.get("use_allowance").equals("true")){
                page.setUseAllowanceForLeave(toEdit, true);
            } else {
                page.setUseAllowanceForLeave(toEdit, false);
            }
        }

        if (StringUtils.isNotEmpty((data.get("limit")))){
            page.setLimit(toEdit, Integer.parseInt(data.get("limit")));
        }
        page.clickSaveButton();
    }

    @When("I add new leave type:")
    public void iAddNewLeaveType(DataTable table) {
        Map<String, String> map = table.transpose().asMap(String.class, String.class);
        AddNewLeaveTypeModal modal = new GeneralSettingsPage(world.driver).leaveTypesSettings.clickAddButton();
        modal.setName(map.get("name"));
        modal.setAllowance(Objects.equals(map.get("use_allowance"), "true"));
        modal.setLimit(map.get("limit"));
        modal.setColor(StringUtils.capitalize(map.get("color")));
        modal.clickCreateButton();
    }

    @When("I delete all leave types")
    public void iDeleteAllLeaveTypes() {
    }

    @When("I delete {string} leave type")
    public void iDeleteLeaveType(String leaveTypeName) {
        LeaveTypesSettings page = new LeaveTypesSettings(world.driver);
        page.deleteLeave(leaveTypeName);
    }

    @Given("following leave type is created:")
    public void followingLeaveTypeIsCreated(DataTable table) {
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        LeaveType leaveType = new LeaveType(data);
        leaveTypeService.insertLeaveTypes(world.currentCompany.getId(), leaveType);
    }
}
