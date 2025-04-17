package pageObjects;

import org.openqa.selenium.WebDriver;

public class ShiftManagementPageObject extends BasePage {
    private WebDriver driver;

    // Locators
    private String hamburgerMenuIcon = "//a[@title='Menu']//i[contains(@class, 'fa-bars')]";
    private String openShiftTitlePopup = "//span[@class='title-name' and text()='Mở ca làm việc']";
    private String openShiftAmountInput = "//label[contains(text(), 'Tiền mặt đầu ca')]/following::input[contains(@class, 'payingAmt') and not(@disabled)][1]";
    private String openShiftButton = "//button[.//span[text()='Mở ca']]";
    private String closeShiftMenuLink = "//li[a[span[i[contains(@class, 'fa-lock')]] and span[normalize-space(text())='Đóng ca làm việc']]]";
    private String closeShiftTitlePopup = "//h4[text()='Phiếu bàn giao ca:']";
    private String closeShiftAmountInput = "//input[@placeholder='Nhập số tiền mặt đang có cuối ca']";
    private String closeShiftButton = "//button[contains(@class, 'btn-fade') and normalize-space(text())='Đóng ca']";

    public ShiftManagementPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public boolean isHamburgerMenuDisplayed() {
        waitForElementVisible(hamburgerMenuIcon);
        return isElementDisplayed(hamburgerMenuIcon);
    }

    public boolean isOpenShiftPopupDisplayed() {
        waitForElementVisible(openShiftTitlePopup);
        return isElementDisplayed(openShiftTitlePopup);
    }

    public void inputOpenShiftAmount(String amount) {
        waitForElementVisible(openShiftAmountInput);
        sendKeysToElement(openShiftAmountInput, amount);
    }

    public void clickOpenShiftButton() {
        waitForElementClickable(openShiftButton);
        clickToElement(openShiftButton);
    }

    public void clickHamburgerMenu() {
        waitForElementClickable(hamburgerMenuIcon);
        clickToElement(hamburgerMenuIcon);
    }

    public void clickCloseShiftLink() {
        waitForElementClickable(closeShiftMenuLink);
        clickToElement(closeShiftMenuLink);
    }

    public boolean isCloseShiftPopupDisplayed() {
        waitForElementVisible(closeShiftTitlePopup);
        return isElementDisplayed(closeShiftTitlePopup);
    }

    public void inputCloseShiftAmount(String amount) {
        waitForElementVisible(closeShiftAmountInput);
        sendKeysToElement(closeShiftAmountInput, amount);
    }

    public void clickCloseShiftButton() {
        waitForElementClickable(closeShiftButton);
        clickToElement(closeShiftButton);
    }

    public boolean isCloseShiftMenuLinkDisplayed() {
        waitForElementVisible(closeShiftMenuLink);
        return isElementDisplayed(closeShiftMenuLink);
    }
}
