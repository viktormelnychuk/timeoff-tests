package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.pages.partials.modals.RemoveCompanyModal;
import com.viktor.timeofftests.pages.partials.settings.BankHolidaySettings;
import com.viktor.timeofftests.pages.partials.settings.CompanyScheduleSettings;
import com.viktor.timeofftests.pages.partials.settings.CompanySettings;
import com.viktor.timeofftests.pages.partials.settings.LeaveTypesSettings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GeneralSettingsPage extends BasePage {
    private WebDriver driver;
    public CompanySettings companySettings;
    public CompanyScheduleSettings companyScheduleSettings;
    public LeaveTypesSettings leaveTypesSettings;
    public BankHolidaySettings bankHolidaySettings;
    private By removeCompanyButton = By.xpath("//button[@data-target='#remove_company_modal']");
    private By alert = By.xpath("//div[@role='alert']");
    public GeneralSettingsPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        this.companySettings = new CompanySettings(driver);
        this.companyScheduleSettings = new CompanyScheduleSettings(driver);
        this.leaveTypesSettings = new LeaveTypesSettings(driver);
        this.bankHolidaySettings = new BankHolidaySettings(driver);
    }
    public RemoveCompanyModal clickDeleteCompanyButton(){
        clickButton(removeCompanyButton);
        return new RemoveCompanyModal(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:3000/settings/general/";
    }
    public String getAlertText(){return findOne(alert).getText();}


}
