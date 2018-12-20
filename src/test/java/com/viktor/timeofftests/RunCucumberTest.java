package com.viktor.timeofftests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"com.viktor.timeofftests.steps","com.viktor.timeofftests.common"}
)
public class RunCucumberTest {
}