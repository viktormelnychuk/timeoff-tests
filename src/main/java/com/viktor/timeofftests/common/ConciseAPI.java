package com.viktor.timeofftests.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public abstract class ConciseAPI {
    public abstract WebDriver getDriver();
    public void open(String url){
        getDriver().get(url);
    }
}
