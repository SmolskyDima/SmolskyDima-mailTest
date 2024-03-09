package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.pages.pagecomponents.ContextMenu;
import org.example.pages.pagecomponents.LeftPanel;
import org.example.pages.pagecomponents.NavigationBar;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class DocumentsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NavigationBar navigationBar;
    private static final Logger logger = LogManager.getLogger(DocumentsPage.class);
    private final By elementThatShouldDisappear = By.cssSelector("div.GCSDBRWBFY.GCSDBRWBGY");
    private final String receivedElementLocator = "//div[contains(@title, '%s')]";
    private final String trashElementLocator = "//div[@class='GCSDBRWBAKB' and contains(text(), '%s')]";


    public DocumentsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navigationBar = new NavigationBar(driver);
    }

    public WebElement getDeleteButton() {
        ContextMenu contextMenu = new ContextMenu(driver);
        return contextMenu.getDeleteElementButton();
    }

    public void clickDocumentsButton() {
        logger.info("Start method clickDocumentsButton");
        WebElement docsButton = navigationBar.getDocumentsButton();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
        Boolean until = wait.until(ExpectedConditions.invisibilityOfElementLocated(elementThatShouldDisappear));
        if (!until) {
            System.out.println("Element is not clickable");
            return;
        }
        docsButton.click();
    }

    public WebElement getReceivedElement(String uniqueName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(String.format(receivedElementLocator, uniqueName))));
    }

    public void sleepForTwoSeconds() {
        logger.info("Start method sleepForTwoSeconds");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEmailFromDocumentsWithRightClick(String uniqueName) {
        logger.info("Start method deleteEmailFromDocumentsWithRightClick");
        Actions actions = new Actions(driver);
        actions.contextClick(getReceivedElement(uniqueName)).perform();

        WebElement deleteFromDocumentElement = getDeleteButton();
        deleteFromDocumentElement.click();
    }

    public void openTrash() {
        logger.info("Start method openTrash");
        LeftPanel leftPanel = new LeftPanel(driver);
        leftPanel.getTrashButton().click();
    }


    private boolean isElementPresentInTrash(String uniqueName) {
        try {
            driver.findElement(By.xpath(String.format(trashElementLocator, uniqueName)));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void assertElementIsPresentInTrash(String uniqueName) {
        logger.info("Start method assertElementIsPresentInTrash");
        boolean isElementPresent = isElementPresentInTrash(uniqueName);
        Assert.assertTrue(isElementPresent, "The element is not present in the Trash");
    }
}