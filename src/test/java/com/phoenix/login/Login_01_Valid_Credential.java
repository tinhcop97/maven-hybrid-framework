package com.phoenix.login;

import commons.BaseTest;
import commons.Constants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.LoginPageObject;
import java.time.Duration;

public class Login_01_Valid_Credential extends BaseTest {
    private LoginPageObject loginPage;
    private String loginUrl;
    private String retailer = Constants.DEFAULT_RETAILER;
    private String username = Constants.DEFAULT_USERNAME;
    private String password = Constants.DEFAULT_PASSWORD;

    @BeforeClass
    public void beforeClass() {
        driver = getBrowserDriver("chrome");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        
        loginPage = new LoginPageObject(driver);
        // Construct login URL dynamically based on configuration
        loginUrl = Constants.BASE_URL + retailer + "/pos/#/login";
    }
    
    @Test
    public void Login_01_Valid_Email_Password() {
        loginPage.openLoginPage(loginUrl);
        loginPage.loginToSystem(retailer, username, password);
        // Add verification points here after implementing them in LoginPageObject
    }
}
