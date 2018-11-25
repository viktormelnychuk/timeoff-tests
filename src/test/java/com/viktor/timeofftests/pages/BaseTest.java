package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.db.DBUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
@Log4j2
public abstract class BaseTest extends ConciseAPI {
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


    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }

    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        log.info("Asserting that [{}] [{}]", actual, matcher.toString());
        MatcherAssert.assertThat(reason, actual, matcher);
    }

}
