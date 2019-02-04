package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
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

    public void fillCompanyName(String company){
        fillInputField(companyNameInp, company);
    }

    public void clickDeleteButton(){
        clickButton(deleteCompanyButton);
    }

    @Override
    public String getBaseUrl() {
        return null;
    }


}
