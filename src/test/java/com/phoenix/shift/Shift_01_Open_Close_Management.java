package com.phoenix.shift;

import commons.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.LoginPageObject;
import pageObjects.RoomPageObject;
import pageObjects.ShiftManagementPageObject;
import services.APIService;

public class Shift_01_Open_Close_Management extends BaseTest {
    private LoginPageObject loginPage;
    private ShiftManagementPageObject shiftPage;
    private RoomPageObject roomPage;
    private APIService apiService;
    private String loginUrl = "https://phoenix.kvfnb.vip/tinhcoinew/pos/#/login";
    private String retailer = "tinhcoinew";
    private String username = "admin";
    private String password = "123";
    private boolean hasActiveShift;

    private void logTime(String operation) {
        System.out.println("[TIME] " + operation + ": " + System.currentTimeMillis());
    }

    @Parameters("browser")
    @BeforeClass
    public void beforeClass(@Optional("chrome") String browserName) {
        try {
            logTime("Start beforeClass");
            super.beforeClass(browserName);
            loginPage = new LoginPageObject(driver);
            shiftPage = new ShiftManagementPageObject(driver);
            
            // Authenticate and get session info
            logTime("Start API authentication");
            apiService = new APIService(retailer);
            apiService.authenticate(username, password);
            logTime("End API authentication");
            
            // Check if there's an active working shift
            logTime("Start checking active shift");
            hasActiveShift = apiService.hasActiveWorkingShift();
            System.out.println("Active shift status: " + (hasActiveShift ? "Has active shift" : "No active shift"));
            logTime("End checking active shift");
            
            // Login to UI first
            logTime("Start UI login");
            loginPage.openLoginPage(loginUrl);
            loginPage.loginToSystem(retailer, username, password);
            roomPage = new RoomPageObject(driver);
            roomPage.waitForLoadingIconToDisappear();
            logTime("End UI login");
            
            logTime("End beforeClass");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize test: " + e.getMessage());
        }
    }

    @Test
    public void TC_01_Manage_Shift_Flow() {
        try {
            logTime("Start TC_01_Manage_Shift_Flow");
            if (hasActiveShift) {
                System.out.println("Found active shift - Closing it first");
                // Close the active shift
                logTime("Start closing active shift");
                shiftPage.clickHamburgerMenu();
                shiftPage.clickCloseShiftLink();
                Assert.assertTrue(shiftPage.isCloseShiftPopupDisplayed(), "Close shift popup should be displayed");
                
                shiftPage.inputCloseShiftAmount("1000000");
                shiftPage.clickCloseShiftButton();
                logTime("End closing active shift");
                
                // Verify open shift popup is displayed after closing
                logTime("Start verifying open shift popup after close");
                Assert.assertTrue(shiftPage.isOpenShiftPopupDisplayed(), "Open shift popup should be displayed after closing shift");
                logTime("End verifying open shift popup after close");
            } else {
                System.out.println("No active shift - Starting with opening a new shift");
            }
            
            // Open a new shift
            logTime("Start opening new shift");
            Assert.assertTrue(shiftPage.isOpenShiftPopupDisplayed(), "Open shift popup should be displayed");
            shiftPage.inputOpenShiftAmount("1000000");
            shiftPage.clickOpenShiftButton();
            logTime("End opening new shift");
            
            // Verify shift is opened by checking close shift menu
            logTime("Start verifying shift opened");
            shiftPage.clickHamburgerMenu();
            Assert.assertTrue(shiftPage.isCloseShiftMenuLinkDisplayed(), "Close shift menu link should be displayed after opening shift");
            logTime("End verifying shift opened");
            
            // Close the shift
            logTime("Start closing shift");
            shiftPage.clickCloseShiftLink();
            Assert.assertTrue(shiftPage.isCloseShiftPopupDisplayed(), "Close shift popup should be displayed");
            
            shiftPage.inputCloseShiftAmount("1000000");
            shiftPage.clickCloseShiftButton();
            logTime("End closing shift");
            
            // Verify shift is closed by checking open shift popup
            logTime("Start verifying shift closed");
            Assert.assertTrue(shiftPage.isOpenShiftPopupDisplayed(), "Open shift popup should be displayed after closing shift");
            logTime("End verifying shift closed");
            logTime("End TC_01_Manage_Shift_Flow");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed: " + e.getMessage());
        }
    }
}
