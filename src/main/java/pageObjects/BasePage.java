package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class BasePage {
    private WebDriver driver;
    private final long longTimeout = 30;
    private final long shortTimeout = 5;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriverWait getExplicitWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
    }

    protected WebDriverWait getShortWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
    }

    protected void openPageUrl(String pageUrl) {
        try {
            driver.get(pageUrl);
            
            // Add this line to optimize page load
            waitForPageLoadComplete(10);
        } catch (Exception e) {
            System.out.println("Failed to load page: " + e.getMessage());
            throw e;
        }
    }
    
    protected void waitForPageLoadComplete(long timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(
            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    protected WebElement getWebElement(String locator) {
        return driver.findElement(getByLocator(locator));
    }

    protected void clickToElement(String locator) {
        try {
            WebElement element = getWebElement(locator);
            
            // Try direct click first without scrolling or waiting
            try {
                // Only wait up to 2 seconds for element to be clickable
                new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
                element.click();
                return;
            } catch (Exception e) {
                // Continue with more robust methods
            }
            
            // Scroll element into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            
            // Try again after scroll
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                // If normal click fails, try JavaScript click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
        } catch (Exception e) {
            System.out.println("Failed to click element: " + e.getMessage());
            throw e;
        }
    }

    protected void sendKeysToElement(String locator, String textValue) {
        try {
            WebElement element = getWebElement(locator);
            element.clear();
            element.sendKeys(textValue);
        } catch (Exception e) {
            System.out.println("Failed to send keys to element: " + e.getMessage());
            throw e;
        }
    }

    protected void waitForElementVisible(String locator) {
        try {
            // First check if element is already visible to avoid unnecessary waiting
            try {
                WebElement element = driver.findElement(getByLocator(locator));
                if (element.isDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                // Element not immediately available, continue with wait
            }
            
            // Use explicit wait
            getExplicitWait().until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
        } catch (Exception e) {
            System.out.println("Element not visible: " + locator);
            throw e;
        }
    }

    protected void waitForElementClickable(String locator) {
        getExplicitWait().until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
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
        try {
            By loadingIcon = By.xpath("//div[contains(@class, 'spinner--container')]//i[contains(@class, 'icon-loading')]");
            
            // First check if loading icon is present
            try {
                List<WebElement> loadingIcons = driver.findElements(loadingIcon);
                if (loadingIcons.isEmpty() || !loadingIcons.get(0).isDisplayed()) {
                    return; // Loading icon already not visible
                }
            } catch (Exception e) {
                return; // Icon not found, no need to wait
            }
            
            // Use a shorter timeout first
            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loadingIcon));
            } catch (TimeoutException e) {
                // If still not disappeared after 10 seconds, use longer timeout
                new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loadingIcon));
            }
        } catch (Exception e) {
            System.out.println("Loading icon did not disappear: " + e.getMessage());
            // Continue execution even if loading icon doesn't disappear
        }
    }

    protected By getByLocator(String locator) {
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
