package com.viktor.timeofftests.common.partials;

import com.viktor.timeofftests.pages.BasePage;
import com.viktor.timeofftests.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuBar {

    private final WebDriver driver;

    @FindBy(id="me_menu")
    private WebElement meMenu;

    @FindBy(linkText = "Logout")
    private WebElement logOutLink;


    public String getBaseUrl() {
        return null;
    }

    public MenuBar(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public LoginPage logout(){
        this.meMenu.click();
        this.logOutLink.click();
        return new LoginPage(this.driver);
    }
}
