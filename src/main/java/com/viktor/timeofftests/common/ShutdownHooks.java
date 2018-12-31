package com.viktor.timeofftests.common;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.util.List;
@Log4j2
public class ShutdownHooks extends Thread {

    private final List<WebDriver> drivers;

    public ShutdownHooks (List<WebDriver> drivers){
        this.drivers = drivers;
    }

    @Override
    public void run(){
        log.info("Closing drivers");
        for (WebDriver driver : drivers) {
            driver.quit();
        }
    }
}
