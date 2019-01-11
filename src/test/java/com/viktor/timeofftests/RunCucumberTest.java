package com.viktor.timeofftests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"com.viktor.timeofftests.steps","com.viktor.timeofftests.common"},
        plugin = {"io.qameta.allure.cucumber3jvm.AllureCucumber3Jvm"}
)
public class RunCucumberTest {
//
//    @BeforeClass
//    public static void beforeClass(){
//        System.out.println("ASDASDASDASDASDASDASDASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//    }
}