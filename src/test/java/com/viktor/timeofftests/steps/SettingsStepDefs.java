package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.CompanySettingsForm;
import com.viktor.timeofftests.forms.LeaveTypeForm;
import com.viktor.timeofftests.forms.WeeklyScheduleForm;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.AddNewBankHolidayModal;
import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
import com.viktor.timeofftests.pages.partials.modals.RemoveCompanyModal;
import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.ScheduleService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.viktor.timeofftests.matcher.StringMatchers.stringContainsAllSubstringsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@Log4j2
public class SettingsStepDefs {

    private World world;
    private ScheduleService scheduleService;
    private LeaveTypeService leaveTypeService;
    private CompanyService companyService;
    private SettingsSteps settingsSteps;
    private List<String> deletedHolidays = new ArrayList<>();

    public SettingsStepDefs(World world, ScheduleService scheduleService, LeaveTypeService leaveTypeService, CompanyService companyService, SettingsSteps settingsSteps){
        this.world = world;
        this.scheduleService = scheduleService;
        this.leaveTypeService = leaveTypeService;
        this.companyService = companyService;
        this.settingsSteps = settingsSteps;
    }


    @When("I edit company settings with following:")
    public void iEditCompanySettingsWithFollowing(DataTable dataTable) {
        log.info("Starting to edit company settings");
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

        log.info("Done editing company settings");
        companySettings.saveCompanySettings();
        Company actual = companyService.getCompanyWithId(world.editedCompany.getId());
        log.info("Verifying database contains edited company");
        assertEquals(world.editedCompany, actual);
    }

    @When("I edit weekly schedule to:")
    public void iEditWeeklyScheduleTo(DataTable table) {
        log.info("Starting to edit weekly schedule");
        Map<String, String> map = table.transpose().asMap(String.class, String.class);
        CompanyScheduleSettings page = new CompanyScheduleSettings(world.driver);
        for (String s : map.keySet()) {
            page.setDay(s, transofrmTrueAndFalseToBool(map.get(s)));
        }
        page.saveSchedule();
        log.info("Done to edit weekly schedule");
        settingsSteps.validateNewSchedule(table.convert(WeeklyScheduleForm.class, false));
    }

    private boolean transofrmTrueAndFalseToBool(String s){
        return Objects.equals(s, "true");
    }

    @When("I edit {string} leave type to:")
    public void iEditLeaveTypeTo(String toEdit, DataTable table) {
        log.info("Starting to edit leave type with name [{}] with following \r\n", toEdit, table);
        Map<String, String> data = table.transpose().asMap(String.class, String.class);
        int toEditLeaveId = leaveTypeService.getLeaveTypesForCompanyWithId(world.currentCompany.getId())
                .stream()
                .filter(o -> Objects.equals(o.getName(), toEdit))
                .collect(Collectors.toList())
                .get(0)
                .getId();
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
        log.info("Done to edit leave type with name [{}]", toEdit);
        log.info("Validating edit leave type with name [{}]", toEdit);
        LeaveTypeForm form = table.convert(LeaveTypeForm.class, false);
        settingsSteps.validateLeaveType(toEditLeaveId, form);
    }

    @When("I add new leave type:")
    public void iAddNewLeaveType(DataTable table) {
        log.info("Start adding new leave type");
        LeaveTypeForm form = table.convert(LeaveTypeForm.class, false);
        AddNewLeaveTypeModal modal = new GeneralSettingsPage(world.driver).leaveTypesSettings.clickAddButton();
        modal.setName(form.getName());
        modal.setAllowance(form.isUseAllowance());
        modal.setLimit(form.getLimit());
        modal.setColor(form.getColor());
        modal.clickCreateButton();
        settingsSteps.validateLeaveTypeCreated(form);
        log.info("Done adding new leave type");
    }

    @When("I delete {string} leave type")
    public void iDeleteLeaveType(String leaveTypeName) {
        log.info("Start deleting leave type");
        LeaveTypesSettings page = new LeaveTypesSettings(world.driver);
        page.deleteLeave(leaveTypeName);
        settingsSteps.validateLeaveTypeDoesNotExist(leaveTypeName);
        log.info("Done deleting leave type");
    }

    @Given("following leave type is created:")
    public void followingLeaveTypeIsCreated(DataTable table) {
        log.info("Start inserting leave types \r\n {}", table);
        LeaveTypeForm form = table.convert(LeaveTypeForm.class, false);
        Map<String, String> map = table.transpose().asMap(String.class, String.class);
        long count = leaveTypeService.getLeaveTypesForCompanyWithId(world.currentCompany.getId())
                .stream()
                .filter(o -> StringUtils.equals(o.getName(), form.getName()))
                .count();
        if(count == 0){
            leaveTypeService.insertLeaveTypes(world.currentCompany.getId(),
                    new LeaveType(map));
        }
        log.info("Done inserting leave types");
    }

    @When("I edit bank holiday name")
    public void iEditBankHolidayName() throws Exception {
        log.info("Start to edit bank holiday names");
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        Map<Integer, String> editedResult = page.editMultipleHolidayNamesWithRandomString(1,2,3,4);
        page.clickSubmitButton();
        log.info("Done editing bank holiday names");
        settingsSteps.validateEditedBankHolidays(editedResult);
    }

    @When("^I delete (one|multiple) bank holiday$")
    public void iDeleteOneBankHoliday(String one) throws Exception {
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        List<String> deleted = Collections.emptyList();
        if(Objects.equals(one, "one")) {
            log.info("Deleting one bank holiday");
            deleted = page.deleteMultipleHolidays(2);
        } else if(Objects.equals(one, "multiple")) {
            log.info("Deleting multiple bank holidays");
            deleted = page.deleteMultipleHolidays(1,2,3,4);
            this.deletedHolidays = deleted;
        }
        page.clickSubmitButton();
        log.info("Done deleting bank holiday(s)");
        settingsSteps.validateDeletedHolidays(deleted);
    }

    @When("import default holidays")
    public void importDefaultHolidays() {
        log.info("Importing default holidays");
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        String alertOnPage = page.clickImportDefaultButton().getAlertText();
        assertThat(alertOnPage, stringContainsAllSubstringsInAnyOrder(this.deletedHolidays));
        settingsSteps.validateImportedHolidays(this.deletedHolidays);
    }

    @When("I add new bank holiday:")
    public void iAddNewBankHoliday(DataTable table) throws ParseException {
        log.info("Starting to add new bank holiday via UI");
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
        log.info("Done to add new bank holiday via UI");
        settingsSteps.validateBankHolidayCreated(data.get("name"));
    }

    @When("^I delete company with name \"([^\"]*)\"$")
    public void iDeleteCompanyWithName(String arg0) {
        log.info("Starting to delete company with name [{}]", arg0);
        RemoveCompanyModal modal = new GeneralSettingsPage(world.driver).clickDeleteCompanyButton();
        modal.fillCompanyName(arg0);
        modal.clickDeleteButton();
        log.info("Done deleting company with name [{}]", arg0);
    }
}
