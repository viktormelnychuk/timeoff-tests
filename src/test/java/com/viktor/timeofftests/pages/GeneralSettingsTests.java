package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.services.UserService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

public class GeneralSettingsTests extends BaseTest {

    @Test
    void navigateToSettingsPage(){
        UserService.getInstance().createDefaultAdmin();
        GeneralSettingsPage generalSettingsPage = new GeneralSettingsPage(getDriver());
        generalSettingsPage.navigate();
        assertThat(generalSettingsPage.getPageTitle(), is("General settings"));
        assertThat(generalSettingsPage.getBaseUrl(), is(generalSettingsPage.getDriver().getCurrentUrl()));
    }
}
