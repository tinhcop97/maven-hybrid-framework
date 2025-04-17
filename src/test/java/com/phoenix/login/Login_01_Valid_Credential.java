package com.phoenix.login;

import commons.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.LoginPageObject;

public class Login_01_Valid_Credential extends BaseTest {
    private LoginPageObject loginPage;
    private String loginUrl = "https://phoenix.kvfnb.vip/tinhcoinew/pos/#/login";
    private String retailer = "tinhcoinew"; // Thay thế bằng retailer thực tế
    private String username = "admin"; // Thay thế bằng username thực tế
    private String password = "123"; // Thay thế bằng password thực tế
    
    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        super.beforeClass(browserName);
        loginPage = new LoginPageObject(driver);
    }
    
    @Test
    public void Login_01_Valid_Email_Password() {
        loginPage.openLoginPage(loginUrl);
        loginPage.loginToSystem(retailer, username, password);
        // Add verification points here after implementing them in LoginPageObject
    }
}
