package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public abstract class BasePage extends ConciseAPI {

    private WebDriver driver;

    public abstract String getBaseUrl();

    public BasePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open(){
        this.driver.get(getBaseUrl());
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

    public void fillInputField(WebElement element, String value){
        this.logInput(element,value);
        element.clear();
        element.sendKeys(value);
    }

    public void selectOption(WebElement element, String text){
        this.logInput(element,text);
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public void clickButton(WebElement element){
        logClick(element);
        element.click();
    }

    private void logInput(WebElement element, String value){
        Logger logger = LogManager.getLogger(this.getClass());
        String logMessage = "Filling " + element.getTagName() + " field" + " with value \"" + value + "\"";
        logger.debug(logMessage);
    }

    private void logClick(WebElement element){
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Clicking on the <"+ element.getTagName() + "> with text \""+element.getText()+"\"");
    }

}
