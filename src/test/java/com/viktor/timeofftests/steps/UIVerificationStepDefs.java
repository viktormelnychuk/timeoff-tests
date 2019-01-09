package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
@Log4j2
public class UIVerificationStepDefs {

    private World world;
    private SettingsSteps settingsSteps;
    private NavigationSteps navigationSteps;
    private DepartmentsSteps departmentsSteps;
    public UIVerificationStepDefs(World world, SettingsSteps settingsSteps, NavigationSteps navigationSteps, DepartmentsSteps departmentsSteps){
        this.world = world;
        this.settingsSteps = settingsSteps;
        this.navigationSteps = navigationSteps;
        this.departmentsSteps = departmentsSteps;
    }

    @And("^the \"([^\"]*)\" page should be opened$")
    public void pageShouldBeOpened(String page) throws Exception {
        log.info("Verifying page [{}] is opened", page);
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
        log.info("Verifying [{}] alert is displayed", alert);
        String actual = world.driver.findElement(By.xpath("//div[@role='alert' and @class='alert alert-danger']")).getText();
        assertEquals(alert, actual);
    }


    @Then("^\"([^\"]*)\" page should reflect correct information$")
    public void companySettingsPageShouldReflectCorrectInformation(String page) throws Exception {
        page = page.toLowerCase();
        log.info("Validating [{}] page to reflect correct information", page);
        switch (page){
            case "company settings":
                settingsSteps.validateDisplayedCompany(world.editedCompany);
                break;
            case "weekly schedule settings":
                settingsSteps.validateWeeklyScheduleForCompany(world.currentCompany.getId());
                break;
            case "leave types":
                settingsSteps.validateDisplayedLeaveTypes(world.currentCompany.getId());
                break;
            case "departments":
                navigationSteps.navigateToDepartmentsPage();
                departmentsSteps.validateDepartmentsPage();
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
                log.info("Checking that leave type [{}] is present on the new absence popuo", leaveTypeName);
                assertThat(modal.getDisplayedLeaveTypesAsString().toArray(), hasItemInArray(leaveTypeName));
                break;
            case "should not":
                log.info("Checking that leave type [{}] is not present on the new absence popuo", leaveTypeName);
                assertThat(modal.getDisplayedLeaveTypesAsString().toArray(), not(hasItemInArray(leaveTypeName)));
        }

    }

    @Then("displayed bank holidays match holidays in db")
    public void allBankHolidaysAreDisplayedOnThePage() {
        log.info("Validating displayed bank holidays");
        settingsSteps.validateDisplayedBankHolidays();

    }

    @Then("{string} department page should reflect correct information")
    public void departmentPageShouldReflectCorrectInformation(String departmentName) {
        departmentsSteps.validateDepartmentPage(departmentName);
    }
}
