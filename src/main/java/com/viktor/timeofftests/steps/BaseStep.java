package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.ConciseAPI;
import com.viktor.timeofftests.pages.BasePage;
import org.openqa.selenium.WebDriver;

public abstract class BaseStep {
    String getBaseURL(){
        return "http://localhost:3000";
    }

}
