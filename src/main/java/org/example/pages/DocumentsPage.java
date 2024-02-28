package org.example.pages;

import org.example.pages.pagecomponents.NavigationBar;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DocumentsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NavigationBar navigationBar;
    private final By deleteFromDocumentElementLocator = By.xpath("//span[text()='Удалить']");
    private final By elementThatShouldDisappear = By.cssSelector("div.GCSDBRWBFY.GCSDBRWBGY");


    public DocumentsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navigationBar = new NavigationBar(driver);
    }

    public WebElement getDeleteFromDocumentElementLocator() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(deleteFromDocumentElementLocator));
    }

    public void clickDocumentsButton() {
        WebElement docsButton = navigationBar.getDocumentsButton();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
        Boolean until = wait.until(ExpectedConditions.invisibilityOfElementLocated(elementThatShouldDisappear));
        if (!until) {
            System.out.println("Element can not be clickable");
        }
        docsButton.click();
    }

    public void sleepForTwoSeconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEmailFromDocumentWithRightClick(String uniqueName) {
        WebElement elementToDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(String.format("//div[contains(@title, '%s')]", uniqueName))));

        Actions actions = new Actions(driver);
        actions.contextClick(elementToDelete).perform();

        WebElement deleteFromDocumentElement = getDeleteFromDocumentElementLocator();
        deleteFromDocumentElement.click();
    }
}
