package com.viktor.timeofftests.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public abstract class PageObject {
    protected WebDriver driver;
    public String test;

    public PageObject(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    protected void fillInputField(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
    }

    protected void fillInputField(WebElement element, Keys key){
        element.clear();
        element.sendKeys(key);
    }

    protected void fillInputField (WebElement element, int number){
        element.clear();
        element.sendKeys(Integer.toString(number));
    }

    protected void selectOption(Select select, String text) throws Exception {
        int maxTries = 2;
        int count = 0;
        while(true){
            try {
                if (count == 0) {
                    select.selectByVisibleText(text);
                    break;
                } else if (count == 1){
                    select.selectByValue(text);
                    break;
                }
            } catch (Exception e){
                count++;
                if(count == maxTries){
                    throw e;
                }
            }
        }
    }

    protected void selectOption(Select select, int optionIndex) throws Exception {
        this.selectOption(select, Integer.toString(optionIndex));
    }




}
