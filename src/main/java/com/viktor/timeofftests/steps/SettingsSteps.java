package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.forms.WeeklyScheduleForm;
import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.LoginPage;
import com.viktor.timeofftests.pages.SignupPage;
import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import com.viktor.timeofftests.services.BankHolidaysService;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.ScheduleService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.viktor.timeofftests.matcher.CollectionMatchers.containsSubList;
import static com.viktor.timeofftests.matcher.CollectionMatchers.hasAllItemsExcludingProperties;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SettingsSteps {

    private World world;
    private ScheduleService scheduleService;
    private LeaveTypeService leaveTypeService;
    private BankHolidaysService bankHolidaysService;
    public SettingsSteps(World world, ScheduleService scheduleService, LeaveTypeService leaveTypeService, BankHolidaysService bankHolidaysService){
        this.world = world;
        this.scheduleService = scheduleService;
        this.leaveTypeService = leaveTypeService;
        this.bankHolidaysService = bankHolidaysService;
    }

    public void validateDisplayedCompany (Company expected){
        CompanySettings companySettings = new CompanySettings(world.driver);
        assertEquals(expected.getName(), companySettings.getCompanyName());
        assertEquals(expected.getCountry(), companySettings.getCountry());
        assertEquals(expected.getDateFormat(), companySettings.getDateFormat());
        assertEquals(expected.getTimezone(), companySettings.getTimeZone());
    }

    public void validateWeeklyScheduleForCompany(int companyID) {
        Schedule inDb = scheduleService.getScheduleForCompanyId(companyID);
        Schedule visible = new CompanyScheduleSettings(world.driver).getVisibleSchedule();
        assertThat(inDb, samePropertyValuesAs(visible,"id","companyId","userID"));
    }

    public void validateLeaveTypes(int id) {
        List<LeaveType> inDb = leaveTypeService.getLeaveTypesForCompanyWithId(id);
        LeaveTypesSettings page = new LeaveTypesSettings(world.driver);
        world.inDbLeaveTypes = inDb;
        assertThat(page.getDisplayedLeaveTypes(world.currentCompany.getId()), containsInAnyOrder(inDb.toArray()));
    }

    public void verifyRegisterPage() {
        SignupPage signupPage = new SignupPage(world.driver);
        assertTrue(signupPage.getDriver().getCurrentUrl()
                .contains(TextConstants.RegisterPageConstants.PAGE_URL));
        String expectedError = "Confirmed password does not match initial one";
        assertEquals(expectedError, signupPage.getAlertMessage());
    }

    public void verifyLoginPage() {
        LoginPage page = new LoginPage(world.driver);
        assertTrue(page.getDriver().getCurrentUrl().contains(TextConstants.LoginPageConstants.PAGE_URL));
    }


    public void verifyCalendarPageTexts(){
        CalendarPage page = new CalendarPage(world.driver);
        String expectedGreeting = String.format(TextConstants.CalendarPageConstants.EMPLOYEE_GREETING_F,
                world.currentUser.getName(), world.currentUser.getLastName());
        assertEquals(page.getEmployeeGreeting(), expectedGreeting);
        assertEquals( TextConstants.CalendarPageConstants.PAGE_URL, page.getDriver().getCurrentUrl());
        assertEquals(TextConstants.CalendarPageConstants.PAGE_TITLE, page.getTitle());
    }

    public void validateDisplayedBankHolidays() {
        BankHolidaySettings page = new BankHolidaySettings(world.driver);
        List<BankHoliday> allDisplayedHolidays = page.getAllDisplayedHolidays();
        List<BankHoliday> inDb = bankHolidaysService.getAllBankHolidaysForCompany(world.currentCompany.getId());
        inDb.sort(Comparator.comparing(BankHoliday::getDate));
        assertThat(allDisplayedHolidays, hasAllItemsExcludingProperties(inDb, "id","companyId"));
    }

    public void validateEditedBankHolidays(Map<Integer, String> editedResult) throws Exception {
        String result = "";
        boolean fail = false;
        List<BankHoliday> inDb = bankHolidaysService.getAllBankHolidaysForCompany(world.currentCompany.getId());
        inDb.sort(Comparator.comparing(BankHoliday::getDate));
        for (Integer integer : editedResult.keySet()) {
            if(!Objects.equals(editedResult.get(integer), inDb.get(integer).getName())){
                result += String.format("expected <%s> but was <%s> \n\r", editedResult.get(integer), inDb.get(integer).getName());
                fail = true;
            }
        }
        if(fail){
            throw new Exception(result);
        }
    }

    public void validateDeletedHolidays(List<String> deleted) throws Exception {
        String result = "";
        boolean fail = false;
        List<BankHoliday> inDb = bankHolidaysService.getAllBankHolidaysForCompany(world.currentCompany.getId());
        List<String> allHolidayNames = inDb.stream().map(BankHoliday::getName).collect(Collectors.toList());
        for (String s : deleted) {
            if(allHolidayNames.contains(s)){
                result += String.format("<%s> bank holiday should not be present in DB", s);
                fail = true;
            }
        }
        if(fail){
            throw new Exception(result);
        }
    }

    public void validateImportedHolidays(List<String> deletedHolidays) {
        List<String> allHolidayNames = bankHolidaysService.getAllBankHolidaysForCompany(world.currentCompany.getId())
                .stream()
                .map(BankHoliday::getName)
                .collect(Collectors.toList());
        assertThat(allHolidayNames, containsSubList(deletedHolidays));
    }

    public void validateBankHolidayCreated(String name) {
        BankHoliday bh = bankHolidaysService.getWithNameForCompany(name, world.currentCompany.getId());
        assertThat(bh, is(notNullValue()));
    }

    public void validateNewSchedule(WeeklyScheduleForm form) {
        Schedule inDb = scheduleService.getScheduleForCompanyId(world.currentCompany.getId());
        assertEquals(inDb.getMonday(), form.getMonday());
        assertEquals(inDb.getTuesday(), form.getTuesday());
        assertEquals(inDb.getWednesday(), form.getWednesday());
        assertEquals(inDb.getThursday(), form.getThursday());
        assertEquals(inDb.getFriday(), form.getFriday());
        assertEquals(inDb.getSaturday(), form.getSaturday());
        assertEquals(inDb.getSunday(), form.getSunday());
    }
}
