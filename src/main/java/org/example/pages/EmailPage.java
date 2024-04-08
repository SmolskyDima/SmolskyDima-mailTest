package org.example.pages;

import lombok.Getter;
import org.example.elements.Button;
import org.example.elements.Element;
import org.example.elements.Input;
import org.example.pages.pagecomponents.SaveDocumentPopup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.nio.file.Path;

import static org.example.driver.Waiter.getWaiter;
import static org.example.driver.Waiter.waitForVisibility;
import static org.example.driver.WebDriverWrapper.getDriver;
import static org.example.utils.Logger.getLogger;

public class EmailPage {

    @Getter
    private static final Button refreshIncomingLettersButton = new Button(By.xpath("//div[@class = 'icon icon16-Refresh']"), "Incoming Letters");
    @Getter
    private static final Button newLetterButton = new Button(By.xpath("//div[@id='mailNewBtn']"), "New letter");
    @Getter
    private static final Input recipientTextBox = new Input(By.className("GCSDBRWBPL"), "Recipient textBox");
    @Getter
    private static final Button attachmentDropDownButton = new Button(By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB"), "Drop down");
    @Getter
    private static final Input attachmentInput = new Input(By.cssSelector("input[type='file'][name^='docgwt-uid-']"), "Attachment input");
    @Getter
    private static final Element fileUploadCheckbox = new Element(By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']"), "uploadCheckbox");
    @Getter
    private static final Element fileUploadProgressBar = new Element(By.xpath("//div[@class='GCSDBRWBCS']"), "uploadProgressBar");
    @Getter
    private static final Input mailSubjectTextBox = new Input(By.id("mailSubject"), "Mail subject");
    @Getter
    private static final Button sendLetterButton = new Button(By.xpath("//div[@id = 'mailSend']"), "Send letter");
    @Getter
    private static final Element toggleButton = new Element(By.xpath("//b[@class='icon-Arrow-down']"), "toggleButton");
    @Getter
    private static final Button saveInDocumentButton = new Button(By.xpath("(//span[@class = 'GCSDBRWBGR'])[2]"), "Save in document");
    @Getter
    private static final String receivedElementLocator = "//div[@class='listSubject' and contains(text(), '%s')]";

    public static Element getReceivedElement(String uniqueName) {
        return new Element(By.xpath(String.format(receivedElementLocator, uniqueName)));
    }


    public static void clickNewLetterButton() {
        getLogger().info("Start method clickNewLetterButton");
        newLetterButton.click();
    }

    public static void enterRecipientEmail(String recipient) {
        getLogger().info("Start method enterRecipientEmail");
        recipientTextBox.sendKeys(recipient, Keys.ENTER);
    }

    public static void attachFileToEmail(Path filePath) {
        getLogger().info("Start method attachFileToEmail");
        attachmentDropDownButton.click();
        String string = filePath.toAbsolutePath().toString();
        attachmentInput.sendKeys(string);
    }

    public static void verifyThatFileIsLoaded() {
        getLogger().info("Start method verifyThatFileIsLoaded");
        try {
            waitForVisibility(fileUploadCheckbox);
        } catch (TimeoutException e) {
            try {
                getWaiter().withMessage("Waiting for file upload completion").until(ExpectedConditions
                        .attributeToBe(fileUploadProgressBar.getLocator(), "style", "width: 100%; visibility: visible;"));
            } catch (TimeoutException second) {
                getLogger().info("Failed to confirm successful file download");
            }
        }
    }

    public static void setEmailSubject(String emailSubject) {
        getLogger().info("Start method setEmailSubject");
        mailSubjectTextBox.sendKeys(emailSubject);
    }

    public static void clickSendLetterButton() {
        getLogger().info("Start method clickSendLetterButton");
        sendLetterButton.click();
    }

    public static void waitUntilEmailReceived(String uniqueName) {
        getLogger().info("Start method waitUntilEmailReceived");
        boolean elementFound = false;
        while (!elementFound) {
            try {
                Element receivedElement = getReceivedElement(uniqueName);
                waitForVisibility(receivedElement);
                receivedElement.click();
                elementFound = true;
            } catch (TimeoutException e) {
                try {
                    getDriver().findElement(refreshIncomingLettersButton.getLocator()).click();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void clickSaveDocumentButton() {
        getLogger().info("Start method clickSaveDocumentButton");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", toggleButton.getElement());
        saveInDocumentButton.click();
    }

    public static SaveDocumentPopup getSaveDocumentPopup() {
        return new SaveDocumentPopup();
    }
}
