package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import com.viktor.timeofftests.common.World;
import com.viktor.timeofftests.common.db.DBUtil;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    World world;

    public Hooks (World world){
        this.world = world;
    }

    @Before
    public void beforeHook(){
        DBUtil.cleanDB();
        world.driver = DriverUtil.getDriver(DriverEnum.CHROME);

    }

    @After
    public void afterHook(){
        //world.driver.quit();
    }
}
