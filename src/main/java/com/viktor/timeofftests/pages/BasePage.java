package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.pages.partials.MenuBar;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Objects;

public abstract class BasePage extends ConciseAPI {
    By pageTitle = By.xpath("//h1");

    private WebDriver driver;
    public MenuBar menuBar;

    public abstract String getBaseUrl();

    protected BasePage(){}

    public BasePage(WebDriver driver){
        this.driver = driver;
        menuBar = new MenuBar(driver);
    }
    protected void setDriver(WebDriver driver){
        this.driver = driver;
    }
    public void open(){
        Logger log = LogManager.getLogger(getClass());
        log.info("Navigating to {}", getBaseUrl());
        this.driver.get(getBaseUrl());
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

   protected void fillInputField(By locator, String value){
        WebElement element = findOne(locator);
        element.clear();
        element.sendKeys(value);
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Filling [{}] found [{}] with value '{}'", element.getTagName(), locator.toString(), value);
    }
   protected void selectOption(By locator, String text) throws Exception {
        WebElement element = findOne(locator);
        Logger logger = LogManager.getLogger(this.getClass());
        Select select = new Select(element);

        List<WebElement> allOptions = select.getOptions();
        boolean selected = false;
        for (WebElement option : allOptions) {
            if(StringUtils.equals(option.getAttribute("value"), text)){
                select.selectByValue(text);
                logger.debug("Filling [{}] found [{}] with value [{}]", element.getTagName(), locator.toString(), text);
                selected = true;
                break;
            }
            if(StringUtils.startsWith(option.getText(), text)){
                select.selectByValue(option.getAttribute("value"));
                logger.debug("Filling [{}] found [{}] with text [{}]", element.getTagName(), locator.toString(), text);
                selected = true;
                break;
            }
        }
        if(!selected){
            throw new Exception("Could not find option");
        }
   }

   protected void setCheckBox(By locator, boolean b){
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Setting checkbox found [{}] to [{}]", locator, b);
        WebElement element = findOne(locator);
        if(element.isSelected() != b){
            clickButton(locator);
        }
   }

   protected void clickButton(By locator){
        WebElement element = findOne(locator);
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Clicking on the <{}> found by [{}]", element.getTagName(), locator);
        element.click();
   }

   protected String getInputValue (By locator) {
        Logger logger = LogManager.getLogger(this.getClass());
        logger.debug("Getting value of input field {}",locator);
        return findOne(locator).getAttribute("value");
   }
   protected String getSelectedOption(By locator){
        Select select = new Select(findOne(locator));
        return select.getFirstSelectedOption().getText();
   }

   protected String getSelectedOptionValue(By locator){
        Select select = new Select(findOne(locator));
        return select.getFirstSelectedOption().getAttribute("value");
   }

   protected boolean getBoolFromYesNo(String word){
        return Objects.equals(word, "Yes");
    }
}
