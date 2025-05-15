package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        System.out.println("Optimizing UI login process...");
        long startTime = System.currentTimeMillis();
        
        try {
            inputRetailer(retailer);
            System.out.println("Retailer input complete: " + (System.currentTimeMillis() - startTime) + "ms");
            
            inputUsername(username);
            System.out.println("Username input complete: " + (System.currentTimeMillis() - startTime) + "ms");
            
            inputPassword(password);
            System.out.println("Password input complete: " + (System.currentTimeMillis() - startTime) + "ms");
            
            // Các kỹ thuật tối ưu thêm
            // 1. Sử dụng JavaScript để tập trung vào các phần tử trước khi tương tác với chúng
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("if(document.activeElement !== null) document.activeElement.blur();");
            
            clickLoginButton();
            System.out.println("Login button clicked: " + (System.currentTimeMillis() - startTime) + "ms");
            
            // Chờ đợi cho đến khi trình duyệt bắt đầu chuyển trang - dấu hiệu của phản hồi
            try {
                // Wait for page to start changing - faster than waiting for complete load
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("loading"));
                System.out.println("Page loading started: " + (System.currentTimeMillis() - startTime) + "ms");
            } catch (Exception e) {
                // Page might have loaded too quickly, or using another loading method
                System.out.println("Could not detect loading state change: " + e.getMessage());
            }
            
            long loginTime = System.currentTimeMillis() - startTime;
            System.out.println("Total UI login execution time: " + loginTime + "ms");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
    
    protected By getByLocator(String locator) {
        if (locator.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        } else if (locator.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("id=")) {
            return By.id(locator.substring(3));
        } else {
            throw new IllegalArgumentException("Unsupported locator format: " + locator);
        }
    }
}
