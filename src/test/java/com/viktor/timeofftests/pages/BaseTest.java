package com.viktor.timeofftests.pages;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.db.DBUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

public class BaseTest extends ConciseAPI {
    @Override
    public WebDriver getDriver() {
        return DriverUtil.getDriver(DriverEnum.CHROME);
    }
    @BeforeClass
   public static void cleanDB(){
        DBUtil.cleanDB();
    }

    @After
    public void tearDown(){
        System.out.print("After Each");
    }

}
