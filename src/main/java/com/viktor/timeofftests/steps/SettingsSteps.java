package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;

import static org.junit.Assert.*;
public class SettingsSteps {

    private World world;

    public SettingsSteps (World world){
        this.world = world;
    }

    public void validateDisplayedCompany (Company expected){
        CompanySettings companySettings = new CompanySettings(world.driver);
        assertEquals(expected.getName(), companySettings.getCompanyName());
        assertEquals(expected.getCountry(), companySettings.getCountry());
        assertEquals(expected.getDateFormat(), companySettings.getDateFormat());
        assertEquals(expected.getTimezone(), companySettings.getTimeZone());
    }
}
