package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
import com.viktor.timeofftests.services.ScheduleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CompanyScheduleSettings extends BasePage {

    private WebDriver driver;

    private By companyWeekScheduleForm = By.id("company_schedule_form");
    private By saveScheduleButton = By.xpath("//form[@id='company_schedule_form']//button[@type='submit']");
    private By title = By.xpath("//form[@id='company_schedule_form']//div[@class='form-group']/label[@class='col-md-6 control-label']");
    private By description = By.xpath("//form[@id='company_schedule_form']//div[@class='form-group']/div[@class='col-md-offset-2']/em");
    private By scheduleButtons = By.xpath("//form[@id='company_schedule_form']//div[@class='btn-group' and @data-toggle='buttons']/label");
    public CompanyScheduleSettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

//    public Schedule getSchedule(){
//        WebElement form = findOne(companyWeekScheduleForm);
//        List<WebElement> elementList = form.findElements(By.xpath("//div[@class='btn-group' and @data-toggle='buttons']/label/input"));
//        return ScheduleService.getInstance().deserializeSchedule(elementList);
//    }
    public CompanyScheduleSettings toggleDay (int index){
        String locator = String.format("//div[@class='btn-group' and @data-toggle='buttons']/label[%d]", index);
        findOne(By.xpath(locator)).click();
        return this;
    }

    public CompanyScheduleSettings toggleDays (int... indexes){
        for (int index : indexes) {
            toggleDay(index);
        }
        return this;
    }

    public String getTitle(){
        return findOne(title)
                .getText();
    }

    public String getDescription(){
        return findOne(description).getText();
    }

    public String getSaveButtonTitle(){
        return findOne(saveScheduleButton).getText();
    }

    public String[] getScheduleDayTitles(){
        List<WebElement> buttons = findAllBy(scheduleButtons);
        String[] buttonTexts = new String[buttons.size()];
        for (int i = 0; i < buttons.size(); i++){
            buttonTexts[i] = buttons.get(i).getText();
        }
        return buttonTexts;
    }

    public GeneralSettingsPage saveSchedule(){
        findOne(saveScheduleButton).click();
        return new GeneralSettingsPage(this.driver);
    }

}
