package org.example.pages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.example.elements.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import static org.example.driver.Waiter.getWaiter;
import static org.example.driver.Waiter.waitForVisibility;
import static org.example.driver.WebDriverWrapper.getDriver;
import static org.example.pages.pagecomponents.ContextMenu.deleteElementButton;
import static org.example.pages.pagecomponents.LeftPanel.trashButton;
import static org.example.pages.pagecomponents.NavigationBar.documentsButton;
import static org.example.utils.Logger.getLogger;

public class DocumentsPage {

    @Getter
    private static final Element elementThatShouldDisappear = new Element(By.cssSelector("div.GCSDBRWBFY.GCSDBRWBGY"), "unknownElement");
    @Getter
    private static final String receivedEmailLocatorTemplate = "//div[contains(@title, '%s')]";
    @Getter
    private static final String trashLocatorTemplate = "//div[@class='GCSDBRWBAKB' and contains(text(), '%s')]";

    @Step("Start method clickDocumentsButton")
    public static void clickDocumentsButton() {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", documentsButton.getElement());
        Boolean until =
                getWaiter().withMessage("Waiting until element disappears").
                        until(ExpectedConditions.invisibilityOfElementLocated(elementThatShouldDisappear.getLocator()));
        if (!until) {
            getLogger().info("Element is not clickable");
            return;
        }
        documentsButton.click();
    }

    public static Element getReceivedEmail(String uniqueName) {
        return new Element(By.xpath(String.format(receivedEmailLocatorTemplate, uniqueName)), "Received email");
    }

    @Step()
    public static void sleepForTwoSeconds() {
        getLogger().info("Start method sleepForTwoSeconds");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Step("Start method deleteEmailFromDocumentsWithRightClick")
    public static void deleteEmailFromDocumentsWithRightClick(String uniqueName) {
        Actions actions = new Actions(getDriver());
        waitForVisibility(getReceivedEmail(uniqueName));
        actions.contextClick(getReceivedEmail(uniqueName).getElement()).perform();
        deleteElementButton.click();
    }
    @Step("Start method openTrash")
    public static void openTrash() {
        trashButton.click();
    }


    private static boolean isElementPresentInTrash(String uniqueName) {
        try {
            getDriver().findElement(By.xpath(String.format(trashLocatorTemplate, uniqueName)));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    @Step("Start method assertElementIsPresentInTrash")
    public static void assertElementIsPresentInTrash(String uniqueName) {
        boolean isElementPresent = isElementPresentInTrash(uniqueName);
        Assert.assertTrue(isElementPresent, "The element is not present in the Trash");
    }
}