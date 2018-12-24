package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.constants.Pages;
import com.viktor.timeofftests.constants.TextConstants;
import com.viktor.timeofftests.models.LeaveType;
import com.viktor.timeofftests.pages.CalendarPage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.LoginPage;
import com.viktor.timeofftests.pages.SignupPage;
import com.viktor.timeofftests.pages.partials.modals.NewAbsenceModal;
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
    public UIVerificationStepDefs(World world, SettingsSteps settingsSteps){
        this.world = world;
        this.settingsSteps = settingsSteps;
    }

    @And("^the \"([^\"]*)\" page should be opened$")
    public void pageShouldBeOpened(String page) throws Exception {
        switch (page.toLowerCase()){
            case Pages.CALENDAR:
                verifyCalendarPageTexts();
                break;
            case Pages.LOGIN:
                verifyLoginPage();
                break;
            case Pages.REGISTER:
                verifyRegisterPage();
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
    private void verifyRegisterPage() {
        SignupPage signupPage = new SignupPage(world.driver);
        assertTrue(signupPage.getDriver().getCurrentUrl()
                .contains(TextConstants.RegisterPageConstants.PAGE_URL));
        String expectedError = "Confirmed password does not match initial one";
        assertEquals(expectedError, signupPage.getAlertMessage());
    }

    private void verifyLoginPage() {
        LoginPage page = new LoginPage(world.driver);
        assertTrue(page.getDriver().getCurrentUrl().contains(TextConstants.LoginPageConstants.PAGE_URL));
        assertEquals( TextConstants.LoginPageConstants.ERROR_MESSAGE, page.getAlertMessage());
    }


    private void verifyCalendarPageTexts(){
        CalendarPage page = new CalendarPage(world.driver);
        String expectedGreeting = String.format(TextConstants.CalendarPageConstants.EMPLOYEE_GREETING_F,
                world.currentUser.getName(), world.currentUser.getLastName());
        assertEquals(page.getEmployeeGreeting(), expectedGreeting);
        assertEquals( TextConstants.CalendarPageConstants.PAGE_URL, page.getDriver().getCurrentUrl());
        assertEquals(TextConstants.CalendarPageConstants.PAGE_TITLE, page.getTitle());
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

    @Then("{string} leave type should be present on new absence popup")
    public void leaveTypeShouldBePresentOnNewAbsencePopup(String arg0) {
        NewAbsenceModal modal = new GeneralSettingsPage(world.driver).menuBar.openNewAbsenceModal();
        List<String> leaveTypesOnModal = modal.getDisplayedLeaveTypesAsString();
        List<String> allLeaveTypes = world.inDbLeaveTypes.stream()
                .map(LeaveType::getName).collect(Collectors.toList());
        assertThat(leaveTypesOnModal, containsInAnyOrder(allLeaveTypes.toArray()));
    }
}
