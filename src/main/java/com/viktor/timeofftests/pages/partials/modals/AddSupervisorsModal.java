package com.viktor.timeofftests.pages.partials.modals;

import com.viktor.timeofftests.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AddSupervisorsModal extends BasePage {
    private WebDriver driver;

    private By modalHeader = By.id("modal_confirmation_reject_la");
    private By listOfAvailableSupervisors = By.xpath("//label[@class='list-group-item label-plain']");
    private By userChkQuery = By.xpath("//label[@class='list-group-item label-plain']//input[@value='%s']");
    private By addEmployeesButton = By.name("do_add_supervisors");

    public AddSupervisorsModal (WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

    public List<Integer> getAllDisplayedUsers(){
        List<WebElement> displayedList = findAllBy(listOfAvailableSupervisors);
        List<Integer> userIds = new ArrayList<>();
        for (WebElement element : displayedList) {
            String id = element.findElement(By.xpath(".//input")).getAttribute("value");
            userIds.add(Integer.valueOf(id));
        }
        return userIds;
    }

    public String getModalHeader(){
        return findOne(modalHeader).getText();
    }
}
