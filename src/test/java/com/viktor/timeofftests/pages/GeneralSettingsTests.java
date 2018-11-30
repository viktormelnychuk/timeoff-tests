package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.CompanyService;
import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

public class GeneralSettingsTests extends BaseTest {
    private UserService userService = UserService.getInstance();
    private CompanyService companyService = CompanyService.getInstance();
    @Test
    void navigateToSettingsPage(){
        userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        assertThat(generalSettingsPage.getPageTitle(), is("General settings"));
        assertThat(generalSettingsPage.getBaseUrl(), is(generalSettingsPage.getDriver().getCurrentUrl()));
    }

    @Test
    void adminEditsCompanyName(){
        User user = userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        generalSettingsPage = generalSettingsPage
                .setCompanyName("New Company Name")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText(), is(expectedAlert));
        assertThat("New Company Name", is(companyService.getCompanyWithId(user.getCompanyID()).getName()));
    }

    @Test
    void adminEditsCompanyCountry (){
        User user = userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        generalSettingsPage = generalSettingsPage
                .setCompanyCountry("GB")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText(), is(expectedAlert));
        assertThat("GB", is(companyService.getCompanyWithId(user.getCompanyID()).getCountry()));
    }
    @Test
    void adminEditsCompanyDateFormat (){
        User user = userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        generalSettingsPage = generalSettingsPage
                .setCompanyDateFormat("DD/MM/YY")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText(), is(expectedAlert));
        assertThat("DD/MM/YY", is(companyService.getCompanyWithId(user.getCompanyID()).getDateFormat()));
    }
    @Test
    void adminEditsCompanyTimeZone (){
        User user = userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        generalSettingsPage = generalSettingsPage
                .setCompanyTimeZone("Europe/London")
                .saveCompanySettings();
        String expectedAlert = "Company was successfully updated";
        assertThat(generalSettingsPage.getAlertText(), is(expectedAlert));
        assertThat("Europe/London", is(companyService.getCompanyWithId(user.getCompanyID()).getTimezone()));
    }

    @Test
    void adminEditsAllCompanySettings(){
        User user = userService.createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        generalSettingsPage = generalSettingsPage
                .setCompanyName("New Company Name")
                .setCompanyCountry("GB")
                .setCompanyDateFormat("DD/MM/YY")
                .setCompanyTimeZone("Europe/London")
                .saveCompanySettings();

        String expectedAlert = "Company was successfully updated";
        Company company = companyService.getCompanyWithId(user.getCompanyID());
        assertThat(generalSettingsPage.getAlertText(), is(expectedAlert));
        assertThat("New Company Name", is(company.getName()));
        assertThat("GB", is(company.getCountry()));
        assertThat("DD/MM/YY", is(company.getDateFormat()));
        assertThat("Europe/London", is(company.getTimezone()));
    }
    /*
    Add tests for schedule part
     */
}
