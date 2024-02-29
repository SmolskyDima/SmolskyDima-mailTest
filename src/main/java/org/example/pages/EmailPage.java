package org.example.pages;

import org.example.pages.pagecomponents.SaveDocumentPopup;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

public class EmailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final By refreshIncomingLettersLocator = By.xpath("//div[text()='Обновить']");
    private static final By newLetterButtonLocator = By.xpath("//div[text()='Создать']");
    private static final By recipientTextBoxLocator = By.className("GCSDBRWBPL");
    private static final By inputLocator = By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB");
    private static final By attachmentInputLocator = By.cssSelector("input[type='file'][name^='docgwt-uid-']");
    private static final By fileUploadCheckboxLocator = By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']");
    private static final By fileUploadProgressBarLocator = By.xpath("//div[@class='GCSDBRWBCS']");
    private static final By mailSubjectTextLocator = By.id("mailSubject");
    private static final By sendLetterLocator = By.xpath("//div[text()='Отправить']");
    private static final By toggleButtonLocator = By.xpath("//b[@class='icon-Arrow-down']");
    private static final By saveInDocumentLocator = By.xpath("//span[@class and text()='Сохранить в документах']");


    public EmailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement getRefreshIncomingLettersLocator() {
        return driver.findElement(refreshIncomingLettersLocator);
    }

    public WebElement getNewLetterButtonLocator() {
        return driver.findElement(newLetterButtonLocator);
    }

    public WebElement getRecipientTextBoxLocator() {
        return driver.findElement(recipientTextBoxLocator);
    }

    public WebElement getInputLocator() {
        return driver.findElement(inputLocator);
    }

    public WebElement getAttachmentInputLocator() {
        return driver.findElement(attachmentInputLocator);
    }

    public WebElement getFileUploadCheckboxLocator() {
        return driver.findElement(fileUploadCheckboxLocator);
    }

    public WebElement getFileUploadProgressBarLocator() {
        return driver.findElement(fileUploadProgressBarLocator);
    }

    public WebElement getMailSubjectTextLocator() {
        return driver.findElement(mailSubjectTextLocator);
    }

    public WebElement getSendLetterLocator() {
        return driver.findElement(sendLetterLocator);
    }

    public WebElement getToggleButtonLocator() {
        return driver.findElement(toggleButtonLocator);
    }

    public WebElement getSaveInDocumentLocator() {
        return driver.findElement(saveInDocumentLocator);
    }

    public boolean isPageOpened() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(refreshIncomingLettersLocator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickNewLetterButton() {
        getNewLetterButtonLocator().click();
    }

    public void enterRecipientEmail(String recipient) {
        WebElement emailTo = getRecipientTextBoxLocator();
        emailTo.sendKeys(recipient, Keys.ENTER);
    }

    public void attachFileToEmail(Path filePath) {
        getInputLocator().click();
        String string = filePath.toAbsolutePath().toString();
        WebElement attachmentInput = getAttachmentInputLocator();
        attachmentInput.sendKeys(string);

    }

    public void verifyThatFileIsLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(fileUploadCheckboxLocator));
        } catch (TimeoutException e) {
            try {
                wait.until(ExpectedConditions
                        .attributeToBe(fileUploadProgressBarLocator, "style", "width: 100%; visibility: visible;"));
            } catch (TimeoutException second) {
                System.out.println("Failed to confirm successful file download");
            }
        }
    }

    public void setEmailSubject(String emailSubject) {
        WebElement mailSubject = getMailSubjectTextLocator();
        mailSubject.sendKeys(emailSubject);
    }

    public void clickSendLetterButton() {
        getSendLetterLocator().click();
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

    public void clickSaveDocumentButton() {
        WebElement toggleButton = getToggleButtonLocator();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toggleButton);
        getSaveInDocumentLocator().click();
    }

    public SaveDocumentPopup getSaveDocumentPopup() {
        return new SaveDocumentPopup(driver);
    }
}
