package com.viktor.timeofftests.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public abstract class ConciseAPI {
    public abstract WebDriver getDriver();
    public void open(String url){
        getDriver().get(url);
    }

    public void fillInputField(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
    }

    public void selectOption(WebElement element, String text){
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }
}
