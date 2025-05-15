package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import api.AuthAPI;
import api.RetailerAPI;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected AuthAPI authAPI;
    protected RetailerAPI retailerAPI;
    
    @Parameters("browser")
    @BeforeClass
    public void beforeClass(@Optional String browser) {
        if(browser != null) {
            // Chỉ khởi tạo driver nếu có tham số browser
            driver = getBrowserDriver(browser);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().window().maximize();
        }
    }

    protected WebDriver getBrowserDriver(String browserName) {
        browserName = browserName.toLowerCase();
        switch (browserName) {
            case "firefox":
                return new FirefoxDriver();
            case "edge":
                return new EdgeDriver();
            case "chrome":
                return new ChromeDriver();
            default:
                throw new RuntimeException("Browser name is not valid!");
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        // Setup chung cho cả test UI và API
    }

    @AfterSuite
    public void afterSuite() {
        // Cleanup chung
    }

    @BeforeMethod
    public void setup() {
        authAPI = new AuthAPI();
        retailerAPI = new RetailerAPI();
    }
}
