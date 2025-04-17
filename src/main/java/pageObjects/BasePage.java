package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    private WebDriver driver;
    private WebDriverWait explicitWait;
    private final long longTimeout = 30;
    private final long shortTimeout = 15;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
    }

    protected WebDriverWait getShortWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
    }

    protected void openPageUrl(String pageUrl) {
        driver.get(pageUrl);
    }

    protected WebElement getWebElement(String locator) {
        return driver.findElement(getByLocator(locator));
    }

    protected void clickToElement(String locator) {
        WebElement element = getWebElement(locator);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        
        // Scroll element into view
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
        
        // Wait for element to be clickable
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
        
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // If normal click fails, try JavaScript click
            jsExecutor.executeScript("arguments[0].click();", element);
        }
    }

    protected void sendKeysToElement(String locator, String textValue) {
        WebElement element = getWebElement(locator);
        element.clear();
        element.sendKeys(textValue);
    }

    protected void waitForElementVisible(String locator) {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    protected void waitForElementClickable(String locator) {
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    protected void waitForElementInvisible(String locator) {
        getShortWait().until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    protected boolean isElementDisplayed(String locator) {
        try {
            return getWebElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void waitForLoadingIconToDisappear() {
        By loadingIcon = By.xpath("//div[contains(@class, 'spinner--container')]//i[contains(@class, 'icon-loading')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIcon));
    }



    private By getByLocator(String locator) {
        By by = null;
        if (locator.startsWith("id=") || locator.startsWith("ID=") || locator.startsWith("Id=")) {
            by = By.id(locator.substring(3));
        } else if (locator.startsWith("class=") || locator.startsWith("CLASS=") || locator.startsWith("Class=")) {
            by = By.className(locator.substring(6));
        } else if (locator.startsWith("name=") || locator.startsWith("NAME=") || locator.startsWith("Name=")) {
            by = By.name(locator.substring(5));
        } else if (locator.startsWith("css=") || locator.startsWith("CSS=") || locator.startsWith("Css=")) {
            by = By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("xpath=") || locator.startsWith("XPATH=") || locator.startsWith("Xpath=")) {
            by = By.xpath(locator.substring(6));
        } else {
            by = By.xpath(locator);
        }
        return by;
    }
}
