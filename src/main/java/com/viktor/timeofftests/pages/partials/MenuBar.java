package com.viktor.timeofftests.pages.partials;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuBar extends BasePage {

    private final WebDriver driver;
    private By meMenu = By.id("me_menu");
    private By logOutLink = By.linkText("Logout");


    public String getBaseUrl() {
        return null;
    }

    public MenuBar(WebDriver driver) {
        this.driver = driver;
        super.setDriver(driver);
    }
    public LoginPage logout(){
        clickButton(this.meMenu);
        clickButton(this.logOutLink);
        return new LoginPage(this.driver);
    }
}
