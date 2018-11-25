package com.viktor.timeofftests.common;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.UnsupportedEncodingException;

public class DriverUtil {
    private WebDriver driver;

    private DriverUtil(){}

    public static WebDriver getDriver(DriverEnum driverType){
        if (driverType == DriverEnum.FIREFOX){
            return new FirefoxDriver();
        } else if (driverType == DriverEnum.CHROME){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("headless");
            return new ChromeDriver();
        }
        return new FirefoxDriver();
    }

    public static String getDriverCookie(String key, WebDriver driver){
        Cookie cookie = driver.manage().getCookieNamed(key);
        return cookie.getValue();
    }

    public static String getSidFromCookies(WebDriver driver){
        try {
            String cookie = getDriverCookie("connect.sid", driver);
            cookie = java.net.URLDecoder.decode(cookie, "UTF-8");
            String[] all = cookie.split("\\.");
            String sid = all[0].split(":")[1];
            System.out.println(sid);
            return sid;
        } catch (UnsupportedEncodingException ex){
            //fail silently
            return "";
        }
    }
}
