package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.Session;
import com.viktor.timeofftests.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Log4j2
public class DriverUtil {
    private WebDriver driver;
    private static SessionService sessionService = new SessionService();

    public static WebDriver getDriver(DriverEnum driverType){
            log.info("Starting new {} browser", driverType.toString());
            if (driverType == DriverEnum.FIREFOX){
                return new FirefoxDriver();
            } else {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("headless");
                return new ChromeDriver(chromeOptions);
            }
    }

    private static String getDriverCookie(String key, WebDriver driver){
        Cookie cookie = driver.manage().getCookieNamed(key);
        if(Objects.isNull(cookie)){
            return null;
        } else {
            return cookie.getValue();
        }
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

    public static void simulateLoginForUser(int userId, WebDriver driver){
        log.info("Inserting cookies for user with id={}", userId);
        Session s = sessionService.insertNewSessionForUserId(userId);
        setSessionCookie(s, driver);
        log.info("Navigating to Calendar page");
        driver.get("http://localhost:3000/calendar/");
    }

    public static void setSessionCookie(Session session, WebDriver driver){
        Calendar calendar = Calendar.getInstance();
        driver.get("http://localhost:3000/404");
        calendar.roll(Calendar.YEAR, -10);
        Date expiry = calendar.getTime();
        Cookie cookie = new Cookie("connect.sid", session.getSid());
        driver.manage().addCookie(cookie);
    }

    public static boolean sessionCookiePresent(WebDriver driver){
        String cookie = getDriverCookie("connect.sid", driver);
        return Objects.nonNull(cookie);
    }
}
