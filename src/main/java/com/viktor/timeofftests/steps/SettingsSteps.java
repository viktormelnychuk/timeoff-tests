package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.models.Company;
<<<<<<< HEAD
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;

import static org.junit.Assert.*;
public class SettingsSteps {

    private World world;

    public SettingsSteps (World world){
        this.world = world;
=======
import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.services.ScheduleService;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
public class SettingsSteps {

    private World world;
    private ScheduleService scheduleService;
    public SettingsSteps(World world, ScheduleService scheduleService){
        this.world = world;
        this.scheduleService = scheduleService;
>>>>>>> 550d9d688775a7f21b7c2e0bbcc11f16cb9451d6
    }

    public void validateDisplayedCompany (Company expected){
        CompanySettings companySettings = new CompanySettings(world.driver);
        assertEquals(expected.getName(), companySettings.getCompanyName());
        assertEquals(expected.getCountry(), companySettings.getCountry());
        assertEquals(expected.getDateFormat(), companySettings.getDateFormat());
        assertEquals(expected.getTimezone(), companySettings.getTimeZone());
    }
<<<<<<< HEAD
=======

    public void validateWeeklyScheduleForCompany(int companyID) {
        Schedule inDb = scheduleService.getScheduleForCompanyId(companyID);
        Schedule visible = new CompanyScheduleSettings(world.driver).getVisibleSchedule();
        assertEquals(inDb.getMonday(), visible.getMonday());
        assertEquals(inDb.getTuesday(), visible.getTuesday());
        assertEquals(inDb.getWednesday(), visible.getWednesday());
        assertEquals(inDb.getThursday(), visible.getThursday());
        assertEquals(inDb.getFriday(), visible.getFriday());
        assertEquals(inDb.getSaturday(), visible.getSaturday());
        assertEquals(inDb.getSunday(), visible.getSunday());
    }
>>>>>>> 550d9d688775a7f21b7c2e0bbcc11f16cb9451d6
}
