package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RemoveCompanyModal extends BasePage {

    private WebDriver driver;
    private By deleteCompanyButton = By.xpath("//form[@id='remove_company_form']//button[@type='submit']");
    private By companyNameInp = By.id("remove_company_name_confirmation");

    public RemoveCompanyModal(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public RemoveCompanyModal fillCompanyName(String company){
        fillInputField(companyNameInp, company);
        return this;
    }

    public GeneralSettingsPage clickDeleteButtonExpectingFailure(){
        clickButton(deleteCompanyButton);
        return new GeneralSettingsPage(this.driver);
    }

    public LoginPage clickDeleteButtonExpectingSuccess(){
        clickButton(deleteCompanyButton);
        return new LoginPage(this.driver);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }


}
