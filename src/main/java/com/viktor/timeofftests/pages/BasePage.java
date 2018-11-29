package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.pages.partials.MenuBar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public abstract class BasePage extends ConciseAPI {
    By pageTitle = By.xpath("//h1");

    private WebDriver driver;
    MenuBar menuBar;

    public abstract String getBaseUrl();

    protected BasePage(){}

    BasePage(WebDriver driver){
        this.driver = driver;
        menuBar = new MenuBar(driver);
    }
    protected void setDriver(WebDriver driver){
        this.driver = driver;
    }
    void open(){
        Logger log = LogManager.getLogger(getClass());
        log.info("Navigating to {}", getBaseUrl());
        this.driver.get(getBaseUrl());
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

   void fillInputField(By locator, String value){
        WebElement element = findOne(locator);
        element.clear();
        element.sendKeys(value);
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Filling [{}] found [{}] with value '{}'", element.getTagName(), locator.toString(), value);
    }

   void selectOption(By locator, String text){
        WebElement element = findOne(locator);
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Filling [{}] found [{}] with value '{}'", element.getTagName(), locator.toString(), text);
        Select select = new Select(element);
        select.selectByValue(text);
    }

   protected void clickButton(By locator){
        WebElement element = findOne(locator);
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Clicking on the <{}> found by [{}]", element.getTagName(), locator);
        element.click();
    }
}
