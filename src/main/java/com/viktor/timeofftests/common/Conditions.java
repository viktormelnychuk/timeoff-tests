package com.viktor.timeofftests.common;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class Conditions {

    public static ExpectedCondition<Boolean> listNthElementHasText(final List<WebElement> list, final int nthElemnt, final String expectedText ){
        return new ExpectedCondition<Boolean>() {
            private String nthElementText = "";
            public Boolean apply(WebDriver driver) {
                try{
                    nthElementText = list.get(nthElemnt).getText();
                    return nthElementText.contains(expectedText);
                } catch (IndexOutOfBoundsException ex){
                    return false;
                }
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> listSizeIsAtLeast (final List<WebElement> list, final int minimumSize){
        return new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                return list.size() >= minimumSize ? list : null;
            }
        };
    }

    public static ExpectedCondition<Boolean> textIn (final WebElement element, final String text){
        return new ExpectedCondition<Boolean>() {
            private String elemText = "";
            public Boolean apply(WebDriver driver) {
                try {
                    elemText = element.getText();
                    return elemText.contains(text);
                } catch (StaleElementReferenceException ex){
                    return false;
                }
            }

            @Override
            public String toString(){
                return String.format("text ('%s') to be present in element %s\n while actual text is: %s\n", text, element, element.getText());
            }

        };
    }

    // ALIASES
    public static ExpectedCondition<Boolean> valueOF(WebElement element, String text){
        return ExpectedConditions.textToBePresentInElementValue(element, text);
    }

    public static ExpectedCondition<WebElement> visible (WebElement element){
        return ExpectedConditions.visibilityOf(element);
    }

    public static ExpectedCondition<WebElement> visible (By locator){
        return ExpectedConditions.visibilityOfElementLocated(locator);
    }


}
