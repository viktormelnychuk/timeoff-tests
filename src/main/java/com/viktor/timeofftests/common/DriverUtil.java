package com.viktor.timeofftests.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverUtil {
    private WebDriver driver;

    private DriverUtil(){}

    public static WebDriver getDriver(DriverEnum driverType){
        if (driverType == DriverEnum.FIREFOX){
            return new FirefoxDriver();
        } else if (driverType == DriverEnum.CHROME){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("headless");
            return new ChromeDriver(chromeOptions);
        }
        return new FirefoxDriver();
    }
}
