package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.LogConfigurer;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.common.db.DBUtil;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Hooks {

    private static boolean dunit = false;
    World world;
    String dirPath = "";
    public Hooks (World world){
        this.world = world;
    }

    @Before(order = 10)
    public void beforeAll(){
        if(!dunit){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh-mm-ss");
            dirPath = "logs/" + dateFormat.format(new Date()) + "/";
            LogConfigurer.configureLogger(dirPath);
            dunit = true;
        }
    }

    @Before
    public void beforeHook(Scenario scenario) {
        Logger logger = LogManager.getLogger();
        String l = "=============================";
        logger.info(l+"Running: "+scenario.getName()+l);
        DBUtil.cleanDB();
        world.driver = DriverUtil.getDriver(DriverEnum.CHROME);
    }

    @After
    public void afterHook(Scenario scenario) throws IOException {
        if(scenario.isFailed()){
            File srcFile = ((TakesScreenshot)world.driver).getScreenshotAs(OutputType.FILE);
           FileUtils.copyFile(srcFile,new File(dirPath + "screenshot.png"));
        }
        world.driver.quit();
    }
}
