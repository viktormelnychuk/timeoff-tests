package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.db.DBUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

@Log4j2
@ExtendWith(TraceUnitExtension.class)
public abstract class BaseTest extends ConciseAPI {
    private WebDriver driver;
    @Override
    public WebDriver getDriver() {
        this.driver = DriverUtil.getDriver(DriverEnum.CHROME);
        return this.driver;
    }
    @BeforeEach
   public void cleanDB(){
      DBUtil.cleanDB();
    }

    @AfterEach
    public void tearDown(){
        this.driver.quit();
    }
}

class TraceUnitExtension implements AfterEachCallback, BeforeEachCallback {
    private Logger log = LogManager.getLogger(BaseTest.class);
    public void afterEach(ExtensionContext extensionContext) throws Exception {
       log.info("Done running {}:{}", extensionContext.getTestClass().toString(), extensionContext.getDisplayName());
    }

    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        log.info("Begin running {}:{}", extensionContext.getTestClass().toString(),extensionContext.getDisplayName());
    }
}
