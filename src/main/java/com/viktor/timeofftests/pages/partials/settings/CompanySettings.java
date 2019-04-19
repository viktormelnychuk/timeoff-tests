package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CompanySettings extends BasePage {
    private WebDriver driver;

    private By companyNameInp = By.id("input_company_name");
    private By countrySelect = By.id("input_country");
    private By dateFormatSelect = By.id("input_date_format");
    private By timeZoneSelect = By.id("input_time_zone");
    private By saveCompanySettingsButton = By.xpath("//form[@id='company_edit_form']//button[@type='submit']");

    public CompanySettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public void setCompanyName(String value){
        fillInputField(companyNameInp, value);
    }

    public String getCompanyName(){
        return getInputValue(companyNameInp);
    }

    public String getCountry(){
        return getSelectedOptionValue(countrySelect);
    }

    public String getDateFormat(){
        return getSelectedOption(dateFormatSelect);
    }

    public String getTimeZone(){
        return getSelectedOption(timeZoneSelect);
    }

    public void setCompanyCountry (String companyCountry) throws Exception {
        selectOption(countrySelect,companyCountry);
    }

    public void setCompanyDateFormat (String companyDateFormat) throws Exception {
        selectOption(dateFormatSelect, companyDateFormat);
    }

    public void setCompanyTimeZone (String timeZone) throws Exception {
        selectOption(timeZoneSelect, timeZone);
    }

    public void saveCompanySettings(){
        clickButton(saveCompanySettingsButton);
        new GeneralSettingsPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}
