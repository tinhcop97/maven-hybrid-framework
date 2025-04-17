package pageObjects;

import org.openqa.selenium.WebDriver;

public class RoomPageObject extends BasePage {
    private WebDriver driver;
    
    // Page UI Elements
    public static final String LOADING_ICON = "xpath=//div[contains(@class, 'spinner--container')]//i[contains(@class, 'icon-loading')]";

    public RoomPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
}
