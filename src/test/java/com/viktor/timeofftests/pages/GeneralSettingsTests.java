package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.LeaveTypeService;
import com.viktor.timeofftests.services.ScheduleService;
import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralSettingsTests extends BaseTest {
    private UserService userService = UserService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();
    private LeaveTypeService leaveTypeService = LeaveTypeService.getInstance();
    private GeneralSettingsPage generalSettingsPage;
    private User user;
    @BeforeEach
    void prepare(){
        user = userService.createDefaultAdmin();
        generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
    }

    @Test
    void navigateToSettingsPage(){
        assertThat(generalSettingsPage.getPageTitle())
                .isEqualTo("General settings");
        assertThat(generalSettingsPage.getBaseUrl())
                .isEqualTo(generalSettingsPage.getDriver().getCurrentUrl());
    }

    @Test
    void checkCompanySettingsUI(){
        Company company = companyService.getCompanyWithId(user.getCompanyID());
        //create alias
        CompanySettings settings = generalSettingsPage.companySettings;
        assertThat(settings.getCompanyLabel()).isEqualTo("Company name");
        assertThat(settings.getCountryLabel()).isEqualTo("Country");
        assertThat(settings.getDateFormatLabel()).isEqualTo("Date format");
        assertThat(settings.getTimeZoneLabel()).isEqualTo("Time zone");
        assertThat(settings.getCompanyName()).isEqualTo(company.getName());
        assertThat(settings.getCountry()).isEqualTo(company.getCountry());
        assertThat(settings.getDateFormat()).isEqualTo(company.getDateFormat());
        assertThat(settings.getTimeZone()).isEqualTo(company.getTimezone());
    }

    @Test
    void adminEditsCompanyName(){
        generalSettingsPage = generalSettingsPage.companySettings
                .setCompanyName("New Company Name")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText()).isEqualTo(expectedAlert);
        assertThat("New Company Name")
                .isEqualTo(companyService.getCompanyWithId(user.getCompanyID()).getName());
    }

    @Test
    void adminEditsCompanyCountry (){
        generalSettingsPage = generalSettingsPage.companySettings
                .setCompanyCountry("GB")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText()).isEqualTo(expectedAlert);
        assertThat("GB").isEqualTo(companyService.getCompanyWithId(user.getCompanyID()).getCountry());
    }
    @Test
    void adminEditsCompanyDateFormat (){
        generalSettingsPage = generalSettingsPage.companySettings
                .setCompanyDateFormat("DD/MM/YY")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText()).isEqualTo(expectedAlert);
        assertThat("DD/MM/YY").isEqualTo(companyService.getCompanyWithId(user.getCompanyID()).getDateFormat());
    }
    @Test
    void adminEditsCompanyTimeZone (){
        generalSettingsPage = generalSettingsPage.companySettings
                .setCompanyTimeZone("Europe/London")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText()).isEqualTo(expectedAlert);
        assertThat("Europe/London").isEqualTo(companyService.getCompanyWithId(user.getCompanyID()).getTimezone());
    }

    @Test
    void adminEditsAllCompanySettings(){
        generalSettingsPage = generalSettingsPage.companySettings
                .setCompanyName("New Company Name")
                .setCompanyCountry("GB")
                .setCompanyDateFormat("DD/MM/YY")
                .setCompanyTimeZone("Europe/London")
                .saveCompanySettings();

        String expectedAlert = "Company was successfully updated";
        Company company = companyService.getCompanyWithId(user.getCompanyID());
        assertThat(generalSettingsPage.getAlertText()).isEqualTo(expectedAlert);
        assertThat("New Company Name").isEqualTo(company.getName());
        assertThat("GB").isEqualTo(company.getCountry());
        assertThat("DD/MM/YY").isEqualTo(company.getDateFormat());
        assertThat("Europe/London").isEqualTo(company.getTimezone());
    }

    //Check UI
    @Test
    void checkScheduleUI(){
        String[] expectedButtonTitles = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String expectedTitle = "Company week schedule";
        String expectedDescription = "Define company wide weekly schedule. Press correspondent button to toggle working/non-working day.";
        String expectedSaveButtonTitle = "Save schedule";
        String[] actualTitles = generalSettingsPage.companyScheduleSettings.getScheduleDayTitles();
        //assertThat(actualTitles, equalTo(expectedButtonTitles));
        assertThat(actualTitles).containsAll(Arrays.asList(expectedButtonTitles));
        assertThat(generalSettingsPage.companyScheduleSettings.getTitle()).isEqualTo(expectedTitle);
        assertThat(generalSettingsPage.companyScheduleSettings.getDescription()).isEqualTo(expectedDescription);
        assertThat(generalSettingsPage.companyScheduleSettings.getSaveButtonTitle()).isEqualTo(expectedSaveButtonTitle);

    }

    @Test
    void checkDefaultSchedule(){
        Schedule visibleSchedule = generalSettingsPage.companyScheduleSettings.getSchedule();
        assertThat(visibleSchedule).isEqualTo(new Schedule());
    }

    @Test
    void adminEditsWeekSchedule(){
        generalSettingsPage = generalSettingsPage.companyScheduleSettings
                .toggleDays(1,2,3,4)
                .saveSchedule();
        Schedule visibleSchedule = generalSettingsPage.companyScheduleSettings.getSchedule();
        Schedule inDbSchedule = ScheduleService.getInstance().getScheduleForCompanyId(user.getCompanyID());

        assertThat(visibleSchedule)
                .isEqualToIgnoringGivenFields(inDbSchedule,"id","companyId","userID");
        assertThat(generalSettingsPage.getAlertText()).isEqualTo("Schedule for company was saved");
    }

    @Test
    void checkLeaveTypesUI(){
        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
        assertThat(displayedLeaveTypes).containsAll(inDbLeaveTypes);
    }

    @Test
    void editLeaveTypes(){
        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
        generalSettingsPage = leaveTypesSettings
                .editLeaveTypeName("Holiday","New Holiday")
                .setPrimaryLeaveType("Sick Leave")
                .setUseAllowanceForLeave("Sick Leave", true)
                .setLimit("Holiday", 10)
                .clickSaveButton();

        assertThat(generalSettingsPage.getAlertText()).isEqualTo("Changes to leave types were saved");

        assertThat(leaveTypesSettings.getDisplayedLeaveTypesAsString()).contains("New Holiday", "Sick Leave");

        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
        assertThat(displayedLeaveTypes).containsAll(inDbLeaveTypes);

        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
        assertThat(modal.getDisplayedLeaveTypesAsString()).contains("Sick Leave", "New Holiday");
    }

    @Test
    void deleteLeaveType(){
        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
        generalSettingsPage = leaveTypesSettings.deleteLeave("Holiday");

        assertThat(generalSettingsPage.getAlertText()).isEqualTo("Leave type was successfully removed");

        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
        assertThat(inDbLeaveTypes).containsAll(displayedLeaveTypes);

        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
        assertThat(modal.getDisplayedLeaveTypesAsString()).containsExactly("Sick Leave");
    }

    @Test
    void canDeleteAllLeaveTypes(){
        generalSettingsPage = generalSettingsPage.leaveTypesSettings.deleteAllLeaveTypesWithSave();

        assertThat(generalSettingsPage.getAlertText()).isEqualTo("Changes to leave types were saved");
        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
        List<LeaveType> displayedLeaveTypes = generalSettingsPage.leaveTypesSettings.getDisplayedLeaveTypes();
        assertThat(inDbLeaveTypes).containsAll(displayedLeaveTypes);

        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
        assertThat(modal.getDisplayedLeaveTypesAsString()).isEmpty();
    }

    @Test
    void addNewLeaveType(){
        AddNewLeaveTypeModal modal = generalSettingsPage.leaveTypesSettings.clickAddButton();
        generalSettingsPage =
                modal.setName("New leave")
                .setAllowance(true)
                .setLimit(10)
                .clickCreateButton();

        assertThat(generalSettingsPage.getAlertText()).isEqualTo("Changes to leave types were saved");
        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
        List<LeaveType> displayedLeaveTypes = generalSettingsPage.leaveTypesSettings.getDisplayedLeaveTypes();
        assertThat(inDbLeaveTypes).containsAll(displayedLeaveTypes);

        NewAbsenceModal newAbsenceModal = generalSettingsPage.menuBar.openNewAbsenceModal();
        assertThat(newAbsenceModal.getDisplayedLeaveTypesAsString()).contains("Holiday","Sick Leave","New leave");
    }

}
