package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.db.DBUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

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

}
