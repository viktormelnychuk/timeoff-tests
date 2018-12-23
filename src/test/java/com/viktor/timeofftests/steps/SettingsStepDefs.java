package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.forms.CompanySettingsForm;
<<<<<<< HEAD
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
=======
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.services.ScheduleService;
>>>>>>> 550d9d688775a7f21b7c2e0bbcc11f16cb9451d6
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.StringUtils;

<<<<<<< HEAD
public class SettingsStepDefs {

    private World world;

    public SettingsStepDefs(World world){
        this.world = world;
=======
import java.util.Map;
import java.util.Objects;

public class SettingsStepDefs {

    private World world;
    private ScheduleService scheduleService;
    public SettingsStepDefs(World world, ScheduleService scheduleService){
        this.world = world;
        this.scheduleService = scheduleService;
>>>>>>> 550d9d688775a7f21b7c2e0bbcc11f16cb9451d6
    }


    @When("I edit company settings with following:")
    public void iEditCompanySettingsWithFollowing(DataTable dataTable) {
        world.editedCompany = world.currentCompany;
        CompanySettings companySettings = new CompanySettings(world.driver);
        CompanySettingsForm form = dataTable.convert(CompanySettingsForm.class, false);
        if(StringUtils.isNotEmpty(form.getCompanyName())){
            companySettings.setCompanyName(form.getCompanyName());
            world.editedCompany.setName(form.getCompanyName());
        }
        if(StringUtils.isNotEmpty(form.getCountry())){
            companySettings.setCompanyCountry(form.getCountry());
            world.editedCompany.setCountry(form.getCountry());
        }
        if(StringUtils.isNotEmpty(form.getDateFormat())){
            companySettings.setCompanyDateFormat(form.getDateFormat());
            world.editedCompany.setDateFormat(form.getDateFormat());
        }
        if(StringUtils.isNotEmpty(form.getTimezone())){
            companySettings.setCompanyTimeZone(form.getTimezone());
            world.editedCompany.setTimezone(form.getTimezone());
        }
        companySettings.saveCompanySettings();
    }
<<<<<<< HEAD
=======

    @When("I edit weekly schedule to:")
    public void iEditWeeklyScheduleTo(DataTable table) {
        Map<String, String> map = table.transpose().asMap(String.class, String.class);
        CompanyScheduleSettings page = new CompanyScheduleSettings(world.driver);
        for (String s : map.keySet()) {
            page.setDay(s, transofrmTrueAndFalseToBool(map.get(s)));
        }
        page.saveSchedule();
    }

    private boolean transofrmTrueAndFalseToBool(String s){
        return Objects.equals(s, "true");
    }
>>>>>>> 550d9d688775a7f21b7c2e0bbcc11f16cb9451d6
}
