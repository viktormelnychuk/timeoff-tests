package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.db.DBUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

@Log4j2
public class BaseTest extends ConciseAPI {
    private WebDriver driver;
    @Override
    public WebDriver getDriver() {
        this.driver = DriverUtil.getDriver(DriverEnum.CHROME);
        return this.driver;
    }
    @BeforeClass
   public static void cleanDB(){
      DBUtil.cleanDB();
    }

    @After
    public void tearDown(){
        this.driver.quit();
    }


    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting (Description description){
            Logger logger = LogManager.getLogger(description.getClass());
            logger.info("Starting to run {}",description.getDisplayName());
        }
    };

    static void assertTrue(String message, Boolean condition){
        log.info("Asserting condition");
        org.junit.Assert.assertTrue(message, condition);
    }

    static void assertEquals (String expected, String actual){
        log.info("Asserting [{}] equals [{}]", actual, expected);
        org.junit.Assert.assertEquals(expected, actual);
    }

}
