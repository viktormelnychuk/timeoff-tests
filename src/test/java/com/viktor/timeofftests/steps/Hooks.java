package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.DriverEnum;
import com.viktor.timeofftests.common.DriverUtil;
import cucumber.api.java.Before;

public class Hooks {

    World world;

    public Hooks (World world){
        this.world = world;
    }

    @Before
    public void beforeHook(){
        world.driver = DriverUtil.getDriver(DriverEnum.CHROME);
    }
}
