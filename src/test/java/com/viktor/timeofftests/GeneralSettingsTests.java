//package com.viktor.timeofftests.pages;
//
//import com.viktor.timeofftests.common.DriverUtil;
//import com.viktor.timeofftests.models.*;
//import com.viktor.timeofftests.pages.partials.modals.AddNewLeaveTypeModal;
//import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
//import com.viktor.timeofftests.pages.partials.modals.RemoveCompanyModal;
//import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
//import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
//import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
//import com.viktor.timeofftests.services.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.WebDriver;
//
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//
//import static com.viktor.timeofftests.common.Utils.getRandomIndexes;
//import static com.viktor.timeofftests.matcher.StringMatchers.stringContainsAllSubstringsInAnyOrder;
//import static org.hamcrest.Matchers.*;
//
//import static com.viktor.timeofftests.matcher.CollectionMatchers.hasAllItemsExcludingProperties;
//
//public class GeneralSettingsTests extends BaseTest {
//    private UserService userService = UserService.getInstance();
//    private CompanyService companyService = CompanyService.getInstance();
//    private LeaveTypeService leaveTypeService = LeaveTypeService.getInstance();
//    private GeneralSettingsPage generalSettingsPage;
//    private User user;
//    @BeforeEach
//    void prepare(){
//        WebDriver driver = getDriver();
//        user = userService.createDefaultAdmin();
//        DriverUtil.simulateLoginForUser(user.getId(), driver);
//        generalSettingsPage = new GeneralSettingsPage(driver);
//        generalSettingsPage.navigate();
//    }
//
//
//    //Check UI
//    @Test
//    void checkScheduleUI(){
//        String[] expectedButtonTitles = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
//        String expectedTitle = "Company week schedule";
//        String expectedDescription = "Define currentCompany wide weekly schedule. Press correspondent button to toggle working/non-working day.";
//        String expectedSaveButtonTitle = "Save schedule";
//        String[] actualTitles = generalSettingsPage.companyScheduleSettings.getScheduleDayTitles();
//        assertAll(
//                ()-> assertThat(actualTitles, equalTo(expectedButtonTitles)),
//                ()-> assertThat(generalSettingsPage.companyScheduleSettings.getTitle(), is(expectedTitle)),
//                ()-> assertThat(generalSettingsPage.companyScheduleSettings.getDescription(), is(expectedDescription)),
//                ()-> assertThat(generalSettingsPage.companyScheduleSettings.getSaveButtonTitle(),is(expectedSaveButtonTitle))
//        );
//    }
//
//    @Test
//    void checkDefaultSchedule(){
//        Schedule visibleSchedule = generalSettingsPage.companyScheduleSettings.getSchedule();
//        assertThat(visibleSchedule, is(new Schedule()));
//    }
//
//    @Test
//    void adminEditsWeekSchedule(){
//        generalSettingsPage = generalSettingsPage.companyScheduleSettings
//                .toggleDays(1,2,3,4)
//                .saveSchedule();
//        Schedule visibleSchedule = generalSettingsPage.companyScheduleSettings.getSchedule();
//        Schedule inDbSchedule = ScheduleService.getInstance().getScheduleForCompanyId(user.getCompanyID());
//        assertAll(
//                ()->assertThat(visibleSchedule, samePropertyValuesAs(inDbSchedule,"companyId","id","userID")),
//                ()-> assertThat(generalSettingsPage.getAlertText(), is("Schedule for currentCompany was saved"))
//        );
//
//    }
//
//    @Test
//    void checkLeaveTypesUI(){
//        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
//        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
//        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
//        assertThat(displayedLeaveTypes, is(inDbLeaveTypes));
//    }
//
//    @Test
//    void editLeaveTypes(){
//        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
//        generalSettingsPage = leaveTypesSettings
//                .editLeaveTypeName("Holiday","New Holiday")
//                .setPrimaryLeaveType("Sick Leave")
//                .setUseAllowanceForLeave("Sick Leave", true)
//                .setLimit("Holiday", 10)
//                .clickSaveButton();
//        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
//        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
//        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(),is("Changes to leave types were saved")),
//                ()->assertThat(leaveTypesSettings.getDisplayedLeaveTypesAsString(),contains("New Holiday","Sick Leave")),
//                ()->assertThat(inDbLeaveTypes, is(displayedLeaveTypes)),
//                ()->assertThat(modal.getDisplayedLeaveTypesAsString(), contains("Sick Leave", "New Holiday"))
//        );
//    }
//
//    @Test
//    void deleteLeaveType(){
//        LeaveTypesSettings leaveTypesSettings = generalSettingsPage.leaveTypesSettings;
//        generalSettingsPage = leaveTypesSettings.deleteLeave("Holiday");
//        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
//        List<LeaveType> displayedLeaveTypes = leaveTypesSettings.getDisplayedLeaveTypes();
//        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is("Leave type was successfully removed")),
//                ()->assertThat(inDbLeaveTypes, is(displayedLeaveTypes)),
//                ()->assertThat(modal.getDisplayedLeaveTypesAsString(), contains("Sick Leave"))
//        );
//    }
//
//    @Test
//    void canDeleteAllLeaveTypes(){
//        generalSettingsPage = generalSettingsPage.leaveTypesSettings.deleteAllLeaveTypesWithSave();
//        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
//        List<LeaveType> displayedLeaveTypes = generalSettingsPage.leaveTypesSettings.getDisplayedLeaveTypes();
//        NewAbsenceModal modal = generalSettingsPage.menuBar.openNewAbsenceModal();
//        assertAll(
//                ()-> assertThat(generalSettingsPage.getAlertText(), is("Changes to leave types were saved")),
//                ()-> assertThat(inDbLeaveTypes, is(displayedLeaveTypes)),
//                ()-> assertThat(modal.getDisplayedLeaveTypesAsString(), emptyIterable())
//        );
//    }
//
//    @Test
//    void addNewLeaveType(){
//        AddNewLeaveTypeModal modal = generalSettingsPage.leaveTypesSettings.clickAddButton();
//        generalSettingsPage =
//                modal.setName("New leave")
//                .setAllowance(true)
//                .setLimit(10)
//                .clickCreateButton();
//        List<LeaveType> inDbLeaveTypes = leaveTypeService.getLeaveTypesForCompanyWithId(user.getCompanyID());
//        List<LeaveType> displayedLeaveTypes = generalSettingsPage.leaveTypesSettings.getDisplayedLeaveTypes();
//        NewAbsenceModal newAbsenceModal = generalSettingsPage.menuBar.openNewAbsenceModal();
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is("Changes to leave types were saved")),
//                ()->assertThat(inDbLeaveTypes, containsInAnyOrder(displayedLeaveTypes.toArray())),
//                ()->assertThat(newAbsenceModal.getDisplayedLeaveTypesAsString(), containsInAnyOrder("Holiday","Sick Leave","New leave"))
//        );
//    }
//
//    @Test
//    void allHolidaysDisplayed(){
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb,"id","companyId"));
//    }
//
//    @Test
//    void editBankHolidayName(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        List<BankHoliday> displayed = bankHolidaySettings.getAllDisplayedHolidays();
//        int[] indexes = getRandomIndexes(displayed, displayed.size()/2);
//        GeneralSettingsPage generalSettingsPage =
//                bankHolidaySettings
//                .editMultipleHolidayNamesWithRandomString(indexes)
//                .clickSubmitButton();
//        displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void deleteMultipleBankHolidays(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        GeneralSettingsPage generalSettingsPage =
//                bankHolidaySettings
//                .deleteMultipleHolidays(0,1,2,3)
//                .clickSubmitButton();
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void addNewBankHoliday(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        GeneralSettingsPage generalSettingsPage = bankHolidaySettings
//                .clickAddNewButton()
//                .fillName("New Holiday")
//                .fillDate(new Date())
//                .clickCreateButton();
//        List<BankHoliday> displayed = generalSettingsPage.bankHolidaySettings.getAllDisplayedHolidays();
//        List<BankHoliday> inDb = BankHolidaysService.getInstance().getAllBankHolidaysForCompany(user.getCompanyID());
//        inDb.sort(Comparator.comparing(BankHoliday::getDate));
//        assertThat(displayed, hasAllItemsExcludingProperties(inDb, "id","companyId"));
//    }
//
//    @Test
//    void importDefaultBankHolidays(){
//        BankHolidaySettings bankHolidaySettings = generalSettingsPage.bankHolidaySettings;
//        List<BankHoliday> bankHolidays = bankHolidaySettings.getAllDisplayedHolidays();
//        int[] indexes = getRandomIndexes(bankHolidays, 4);
//        bankHolidaySettings.deleteMultipleHolidays(indexes);
//        List<String> deletedHolidays = bankHolidaySettings.getDeletedHolidays();
//        generalSettingsPage = generalSettingsPage.bankHolidaySettings.clickImportDefaultButton();
//        String expected = "New bank holidays were added: ";
//        assertThat(generalSettingsPage.getAlertText(), is(containsString(expected)));
//        assertThat(generalSettingsPage.getAlertText(), stringContainsAllSubstringsInAnyOrder(deletedHolidays));
//    }
//
//    @Test
//    void cannotDeleteCompanyWithIncorrectName(){
//        Company currentCompany = companyService.getCompanyWithId(user.getCompanyID());
//        RemoveCompanyModal removeCompanyModal = generalSettingsPage.clickDeleteCompanyButton();
//        generalSettingsPage = removeCompanyModal
//                .fillCompanyName(currentCompany.getName()+"Random addition")
//                .clickDeleteButtonExpectingFailure();
//        String expectedMessage = "Failed to remove currentCompany. Reason: Provided name confirmation does not match currentCompany one";
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is(expectedMessage)),
//                ()->assertThat(companyService.getCompanyWithId(currentCompany.getId()), is(notNullValue()))
//        );
//    }
//
//    @Test
//    void canDeleteCompanyWithCorrectName(){
//        Company currentCompany = companyService.getCompanyWithId(user.getCompanyID());
//        RemoveCompanyModal removeCompanyModal = generalSettingsPage.clickDeleteCompanyButton();
//        LoginPage loginPage = removeCompanyModal
//                .fillCompanyName(currentCompany.getName())
//                .clickDeleteButtonExpectingSuccess();
//        String expectedMessage = "Company Acme and related data were successfully removed";
//        assertAll(
//                ()->assertThat(generalSettingsPage.getAlertText(), is(expectedMessage)),
//                ()->assertThat(companyService.getCompanyWithId(currentCompany.getId()), is(nullValue())),
//                ()->assertThat(loginPage.getDriver().getCurrentUrl(), is(containsString(loginPage.getBaseUrl()))),
//                ()->assertThat(userService.getUserWithEmail(user.getEmail()), is(nullValue()))
//        );
//    }
//}
