package org.example.pages;

import org.example.utils.ScreenshotsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DocumentsPage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    private final By refreshIncomingLettersLocator = By.xpath("//div[text()='Обновить']");
    private final By deleteFromDocumentElementLocator = By.xpath("//span[text()='Удалить']");


    public DocumentsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void waitUntilEmailReceived(String uniqueName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath(String.format("//div[@class='listSubject' and contains(text(), '%s')]", uniqueName))));
        } catch (TimeoutException e) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(refreshIncomingLettersLocator)).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .cssSelector(String.format("div.listSubject[title='%s']", uniqueName)))).click();
    }

    public void clickDocumentsButton() {
        WebElement docsButton = driver.findElement(By.id("nav-docs"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
        docsButton.click();
    }

    public void sleepForTwoSeconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEmailFromDocument(String uniqueName) {
        WebElement elementToDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(String.format("//div[contains(@title, '%s')]",uniqueName))));

        Actions actions = new Actions(driver);
        actions.contextClick(elementToDelete).perform();

        WebElement deleteFromDocumentElement = wait
                .until(ExpectedConditions.visibilityOfElementLocated(deleteFromDocumentElementLocator));
        deleteFromDocumentElement.click();
    }
}
