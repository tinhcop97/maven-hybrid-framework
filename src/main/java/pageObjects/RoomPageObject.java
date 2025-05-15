package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

public class RoomPageObject extends BasePage {
    private WebDriver driver;
    
    // Page UI Elements
    public static final String LOADING_ICON = "xpath=//div[contains(@class, 'spinner--container')]//i[contains(@class, 'icon-loading')]";
    public static final String PAGE_CONTENT = "css=.content-container";

    public RoomPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
    
    /**
     * Cải tiến phương thức chờ đợi loading icon biến mất với timeout linh hoạt
     * và kiểm tra trước để giảm thiểu thời gian chờ
     */
    public void waitForLoadingIconToDisappear() {
        long startTime = System.currentTimeMillis();
        System.out.println("Waiting for loading icon to disappear...");
        
        // Using the constant LOADING_ICON instead of defining a new XPath
        By loadingIconBy = getByLocator(LOADING_ICON);
        
        try {
            // Kiểm tra nhanh nếu loading icon không hiển thị để tránh chờ đợi không cần thiết
            List<WebElement> loadingIcons = driver.findElements(loadingIconBy);
            if (loadingIcons.isEmpty()) {
                System.out.println("Loading icon not found: " + (System.currentTimeMillis() - startTime) + "ms");
                return;
            }
            
            // Nếu icon hiển thị, sử dụng một wait duy nhất với timeout phù hợp (15 seconds)
            new WebDriverWait(driver, Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.invisibilityOfElementLocated(loadingIconBy));
                
            System.out.println("Loading icon disappeared: " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (TimeoutException e) {
            System.out.println("Loading icon still visible after timeout: " + (System.currentTimeMillis() - startTime) + "ms");
            // Capture screenshot for debugging
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(screenshot.toPath(), new File("loading_icon_timeout_" + System.currentTimeMillis() + ".png").toPath(), 
                         StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot taken for loading icon issue");
            } catch (Exception screenshotError) {
                System.out.println("Failed to take screenshot: " + screenshotError.getMessage());
            }
            System.out.println("Continuing execution despite loading icon issue");
        } catch (Exception e) {
            System.out.println("Failed to wait for loading icon: " + e.getMessage());
            System.out.println("Continuing execution despite loading icon issue");
        }
    }
    
    /**
     * Kiểm tra xem phần tử có hiển thị trong khoảng thời gian chờ cụ thể không
     * @param locator Định vị phần tử
     * @param timeoutInSeconds Thời gian chờ tối đa (giây)
     * @return true nếu phần tử hiển thị, false nếu không
     */
    private boolean isElementDisplayedWithTimeout(String locator, int timeoutInSeconds) {
        try {
            By by = getByLocator(locator);
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Chờ đợi trang đã load hoàn tất và sẵn sàng sử dụng, bao gồm xử lý popup mở ca nếu cần
     */
    public void waitForPageReady() {
        long startTime = System.currentTimeMillis();
        System.out.println("Waiting for page to be ready...");
        
        try {
            // Chờ đợi loading icon biến mất
            waitForLoadingIconToDisappear();
            System.out.println("Loading icon disappeared, checking page state...");
            
            // Kiểm tra xem có popup mở ca hay không (trường hợp status 204)
            String openShiftPopupLocator = "xpath=//div[contains(@class, 'modal') and contains(.,'Mở ca làm việc')]";
            boolean hasOpenShiftPopup = false;
            
            try {
                // Dùng thời gian chờ ngắn để kiểm tra nhanh
                hasOpenShiftPopup = isElementDisplayedWithTimeout(openShiftPopupLocator, 3);
                
                if (hasOpenShiftPopup) {
                    System.out.println("Open shift popup detected (status 204): " + (System.currentTimeMillis() - startTime) + "ms");
                    
                    // Nếu có popup, kiểm tra các phần tử cần thiết và điền thông tin
                    String amountInputLocator = "xpath=//input[@formcontrolname='amount']";
                    String confirmButtonLocator = "xpath=//button[text()='Đồng ý' or text()='Mở ca']";
                    
                    waitForElementVisible(amountInputLocator);
                    getWebElement(amountInputLocator).sendKeys("0");
                    
                    waitForElementClickable(confirmButtonLocator);
                    getWebElement(confirmButtonLocator).click();
                    
                    System.out.println("Shift opening completed: " + (System.currentTimeMillis() - startTime) + "ms");
                    
                    // Sau khi mở ca, chờ loading icon biến mất lần nữa
                    waitForLoadingIconToDisappear();
                } else {
                    System.out.println("No open shift popup (status 200), room page loading directly: " + (System.currentTimeMillis() - startTime) + "ms");
                }
            } catch (Exception e) {
                System.out.println("Error checking for shift popup: " + e.getMessage());
            }
            
            // Chờ đợi nội dung trang phòng bàn hiển thị
            try {
                waitForElementVisible(PAGE_CONTENT);
                System.out.println("Room page content visible: " + (System.currentTimeMillis() - startTime) + "ms");
            } catch (Exception e) {
                System.out.println("Could not verify room page content visibility: " + e.getMessage());
                // Chụp màn hình để gỡ lỗi
                try {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    Files.copy(screenshot.toPath(), new File("room_page_not_ready_" + System.currentTimeMillis() + ".png").toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Screenshot taken for room page not ready issue");
                } catch (Exception screenshotError) {
                    System.out.println("Failed to take screenshot: " + screenshotError.getMessage());
                }
            }
            
            // Đảm bảo trang đã load hoàn tất
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
                System.out.println("Page fully loaded: " + (System.currentTimeMillis() - startTime) + "ms");
            } catch (Exception e) {
                System.out.println("Could not verify complete page load: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Exception during page ready check: " + e.getMessage());
        }
    }
}
