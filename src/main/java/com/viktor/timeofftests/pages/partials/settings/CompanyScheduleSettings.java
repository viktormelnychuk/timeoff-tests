package com.viktor.timeofftests.pages.partials.settings;

import com.viktor.timeofftests.models.Schedule;
import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.GeneralSettingsPage;
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
    private String scheduleButtonsQuery = "//form[@id='company_schedule_form']//input[@type='checkbox'][@name='%s']";
    public CompanyScheduleSettings (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
    public void toggleDay (int index){
        String locator = String.format("//div[@class='btn-group' and @data-toggle='buttons']/label[%d]", index);
        findOne(By.xpath(locator)).click();
    }
    public void setDay (String day, boolean v){
        By locator = By.xpath(String.format(scheduleButtonsQuery, day));
        WebElement element = driver.findElement(locator);
        if(element.isSelected() != v){
            element.findElement(By.xpath("./..")).click();
        }
    }

    public GeneralSettingsPage saveSchedule(){
        findOne(saveScheduleButton).click();
        return new GeneralSettingsPage(this.driver);
    }

    public Schedule getVisibleSchedule() {
        List<WebElement> all = findAllBy(scheduleButtons);
        Schedule schedule = new Schedule();
        for (WebElement element : all) {
            WebElement input = element.findElement(By.xpath("./input"));
            schedule.set(input.getAttribute("name"), input.isSelected());
        }
        return schedule;
    }
}
