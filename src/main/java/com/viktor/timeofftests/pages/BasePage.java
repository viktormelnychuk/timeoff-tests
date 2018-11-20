package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage extends ConciseAPI {

    private WebDriver driver;

    public BasePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public abstract String getBaseUrl();

    public void open(){
        this.driver.get(getBaseUrl());
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }
}
