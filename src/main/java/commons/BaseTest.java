package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

/**
 * Base class for all test classes.
 * Provides common setup and teardown methods.
 */
public class BaseTest {
    protected WebDriver driver;
    
    /**
     * Initializes the test environment before any test suite runs.
     * This includes setting up the session manager to get dynamic values.
     */
    @BeforeSuite
    public void beforeSuite() {
        LogHelper.info("Initializing test environment");
        
        // Initialize session to get dynamic values from current session
        SessionManager.initSession();
        
        LogHelper.info("Test environment initialized");
    }
    
    /**
     * Gets a configured WebDriver instance based on the browser name.
     * @param browserName Name of the browser to use (chrome, firefox, edge)
     * @return Configured WebDriver instance
     */
    protected WebDriver getBrowserDriver(String browserName) {
        LogHelper.info("Setting up " + browserName + " browser");
        
        int pageLoadTimeout = Integer.parseInt(ConfigReader.getProperty("pageLoadTimeout", "60"));
        int scriptTimeout = Integer.parseInt(ConfigReader.getProperty("scriptTimeout", "30"));
        
        if ("chrome".equalsIgnoreCase(browserName)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-geolocation");
            options.addArguments("--disable-popup-blocking");
            
            // Add options to help with connection issues
            options.addArguments("--dns-prefetch-disable");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            // Increase timeouts to avoid connection issues
            driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
            
        } else if ("firefox".equalsIgnoreCase(browserName)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
            
        } else if ("edge".equalsIgnoreCase(browserName)) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
            
        } else {
            throw new RuntimeException("Browser name '" + browserName + "' is not supported");
        }
        
        LogHelper.info(browserName + " browser setup completed");
        return driver;
    }
    
    /**
     * Clean up resources after test class execution.
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (driver != null) {
            LogHelper.info("Closing browser");
            driver.quit();
        }
    }
}
