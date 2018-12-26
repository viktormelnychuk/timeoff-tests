package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.CompanySettingsForm;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewBankHolidayModal;
import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.ScheduleService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.viktor.timeofftests.matcher.StringMatchers.stringContainsAllSubstringsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SettingsStepDefs {

    private World world;
    private ScheduleService scheduleService;
    private LeaveTypeService leaveTypeService;
    private SettingsSteps settingsSteps;
    private List<String> deletedHolidays = new ArrayList<>();

    public SettingsStepDefs(World world, ScheduleService scheduleService, LeaveTypeService leaveTypeService, SettingsSteps settingsSteps){
        this.world = world;
        this.scheduleService = scheduleService;
        this.leaveTypeService = leaveTypeService;
        this.settingsSteps = settingsSteps;
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

    @When("I edit bank holiday name")
    public void iEditBankHolidayName() throws Exception {
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        Map<Integer, String> editedResult = page.editMultipleHolidayNamesWithRandomString(1,2,3,4);
        page.clickSubmitButton();
        settingsSteps.validateEditedBankHolidays(editedResult);
    }

    @When("^I delete (one|multiple) bank holiday$")
    public void iDeleteOneBankHoliday(String one) throws Exception {
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        List<String> deleted = Collections.emptyList();
        if(Objects.equals(one, "one")) {
            deleted = page.deleteMultipleHolidays(2);
        } else if(Objects.equals(one, "multiple")) {
            deleted = page.deleteMultipleHolidays(1,2,3,4);
            this.deletedHolidays = deleted;
        }
        page.clickSubmitButton();
        settingsSteps.validateDeletedHolidays(deleted);
    }

    @When("import default holidays")
    public void importDefaultHolidays() {
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        String alertOnPage = page.clickImportDefaultButton().getAlertText();
        assertThat(alertOnPage, stringContainsAllSubstringsInAnyOrder(this.deletedHolidays));
        settingsSteps.validateImportedHolidays(this.deletedHolidays);
    }

    @When("I add new bank holiday:")
    public void iAddNewBankHoliday(DataTable table) throws ParseException {
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        AddNewBankHolidayModal modal = new BankHolidaySettings(world.driver).clickAddNewButton();
        if(StringUtils.isNotEmpty(data.get("name"))){
            modal.fillName(data.get("name"));
        }
        if(StringUtils.isNotEmpty(data.get("date"))){
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm-DD-YYYY");
            Date date = dateFormat.parse(data.get("date"));
            modal.fillDate(date);
        }
        modal.clickCreateButton();
        settingsSteps.validateBankHolidayCreated(data.get("name"));
    }
}
