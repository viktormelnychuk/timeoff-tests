package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage extends ConciseAPI {

    private WebDriver driver;

    public BasePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }
}
