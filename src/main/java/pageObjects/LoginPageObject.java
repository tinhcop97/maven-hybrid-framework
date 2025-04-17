package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

import java.util.List;

public class LoginPageObject extends BasePage {
    private WebDriver driver;
    
    // Page UI Elements
    private final String RETAILER_TEXTBOX = "xpath=//input[@formcontrolname='retailer']";
    private final String USERNAME_TEXTBOX = "xpath=//input[@formcontrolname='username']";
    private final String PASSWORD_TEXTBOX = "xpath=//input[@formcontrolname='password']";
    private final String LOGIN_BUTTON = "xpath=//button[@type='submit']";

    public LoginPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void openLoginPage(String url) {
        openPageUrl(url);
    }

    public void inputRetailer(String retailer) {
        waitForElementVisible(RETAILER_TEXTBOX);
        sendKeysToElement(RETAILER_TEXTBOX, retailer);
    }

    public void inputUsername(String username) {
        waitForElementVisible(USERNAME_TEXTBOX);
        sendKeysToElement(USERNAME_TEXTBOX, username);
    }

    public void inputPassword(String password) {
        waitForElementVisible(PASSWORD_TEXTBOX);
        sendKeysToElement(PASSWORD_TEXTBOX, password);
    }

    public void clickLoginButton() {
        waitForElementClickable(LOGIN_BUTTON);
        clickToElement(LOGIN_BUTTON);
    }


    public void loginToSystem(String retailer, String username, String password) {
        inputRetailer(retailer);
        inputUsername(username);
        inputPassword(password);
        clickLoginButton();
    }
}
