import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FileActionsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By toggleButtonLocator = By.xpath("//b[@class='icon-Arrow-down']");
    private final By saveInDocumentLocator = By.xpath("//span[@class and text()='Сохранить в документах']");
    private final By clickMyDocumentLocator = By.xpath("//div[text()='Мои документы']");
    private final By saveButtonToBeClickableLocator = By.xpath("//div[@id='dialBtn_OK']");
    private final By clickSaveButtonLocator = By.xpath("//div[@id='dialBtn_OK']");


    public FileActionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickToggleButton() {
        WebElement toggleButton = driver.findElement(toggleButtonLocator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toggleButton);
    }

    public void saveInDocument() {
        driver.findElement(saveInDocumentLocator).click();
    }

    public void clickMyDocuments() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(clickMyDocumentLocator)).click();
    }

    public void waitForSaveButtonToBeClickable() {
        wait.until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButtonToBeClickableLocator, "class", "GCSDBRWBMB")));
    }

    public void clickSaveButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(clickSaveButtonLocator)).click();
    }

}

