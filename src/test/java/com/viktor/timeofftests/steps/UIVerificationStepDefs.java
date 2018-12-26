package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.models.BankHoliday;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.LoginPage;
import com.viktor.timeofftests.pages.SignupPage;
import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
import com.viktor.timeofftests.services.BankHolidaysService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.openqa.selenium.By;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
public class UIVerificationStepDefs {

    private World world;
    private SettingsSteps settingsSteps;
    private BankHolidaysService bankHolidaysService;
    public UIVerificationStepDefs(World world, SettingsSteps settingsSteps){
        this.world = world;
        this.settingsSteps = settingsSteps;
    }

    @And("^the \"([^\"]*)\" page should be opened$")
    public void pageShouldBeOpened(String page) throws Exception {
        switch (page.toLowerCase()){
            case Pages.CALENDAR:
                settingsSteps.verifyCalendarPageTexts();
                break;
            case Pages.LOGIN:
                settingsSteps.verifyLoginPage();
                break;
            case Pages.REGISTER:
                settingsSteps.verifyRegisterPage();
                break;
            default:
                throw new Exception("Page " + page + " is not present in app");
        }
    }

    @Then("^I should see alert \"([^\"]*)\" on the page$")
    public void iShouldSeeAlertAlertOnThePage(String alert) {
        String actual = world.driver.findElement(By.xpath("//div[@role='alert' and @class='alert alert-danger']")).getText();
        assertEquals(alert, actual);
    }


    @Then("^\"([^\"]*)\" page should reflect correct information$")
    public void companySettingsPageShouldReflectCorrectInformation(String page) throws Exception {
        page = page.toLowerCase();
        switch (page){
            case "company settings":
                settingsSteps.validateDisplayedCompany(world.editedCompany);
                break;
            case "weekly schedule settings":
                settingsSteps.validateWeeklyScheduleForCompany(world.currentCompany.getId());
                break;
            case "leave types":
                settingsSteps.validateLeaveTypes(world.currentCompany.getId());
                break;
            default:
                throw new Exception("Page is not known");
        }

    }

    @Then("^\"([^\"]*)\" leave type (should|should not) be present on new absence popup$")
    public void leaveTypeShouldBePresentOnNewAbsencePopup(String leaveTypeName, String should) {
        NewAbsenceModal modal = new GeneralSettingsPage(world.driver).menuBar.openNewAbsenceModal();
        switch (should){
            case "should":
                assertThat(modal.getDisplayedLeaveTypesAsString().toArray(), hasItemInArray(leaveTypeName));
                break;
            case "should not":
                assertThat(modal.getDisplayedLeaveTypesAsString().toArray(), not(hasItemInArray(leaveTypeName)));
        }

    }

    @Then("displayed bank holidays match holidays in db")
    public void allBankHolidaysAreDisplayedOnThePage() {
        settingsSteps.validateDisplayedBankHolidays();

    }
}
