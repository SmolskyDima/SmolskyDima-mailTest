package org.example.pages;

import org.example.pages.pagecomponents.SaveDocumentInModalWindow;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

public class EmailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final SaveDocumentInModalWindow saveDocumentInModalWindow;
    private final By refreshIncomingLettersLocator = By.xpath("//div[text()='Обновить']");
    private final By newLetterButtonLocator = By.xpath("//div[text()='Создать']");
    private final By recipientTextBoxLocator = By.className("GCSDBRWBPL");
    private final By inputLocator = By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB");
    private final By attachmentInputLocator = By.cssSelector("input[type='file'][name^='docgwt-uid-']");
    private final By fileUploadCheckboxLocator = By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']");
    private final By fileUploadProgressBarLocator = By.xpath("//div[@class='GCSDBRWBCS']");
    private final By mailSubjectTextLocator = By.id("mailSubject");
    private final By sendLetterLocator = By.xpath("//div[text()='Отправить']");
    private final By toggleButtonLocator = By.xpath("//b[@class='icon-Arrow-down']");
    private final By saveInDocumentLocator = By.xpath("//span[@class and text()='Сохранить в документах']");


    public EmailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.saveDocumentInModalWindow = new SaveDocumentInModalWindow(driver);
    }

    public void clickNewLetterButton() {
        driver.findElement(newLetterButtonLocator).click();
    }

    public void enterRecipientEmail(String recipient) {
        WebElement emailTo = driver.findElement(recipientTextBoxLocator);
        emailTo.sendKeys(recipient, Keys.ENTER);
    }

    public void attachFileToEmail(Path filePath) {
        driver.findElement(inputLocator).click();
        String string = filePath.toAbsolutePath().toString();
        WebElement attachmentInput = driver.findElement(attachmentInputLocator);
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
        WebElement mailSubject = driver.findElement(mailSubjectTextLocator);
        mailSubject.sendKeys(emailSubject);
    }

    public void clickSendLetterButton() {
        driver.findElement(sendLetterLocator).click();
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

    public void clickAttachedFileInDocument() {
        WebElement toggleButton = driver.findElement(toggleButtonLocator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toggleButton);
        driver.findElement(saveInDocumentLocator).click();
    }

    public SaveDocumentInModalWindow getSaveDocumentInModalWindow() {
        return saveDocumentInModalWindow;
    }


}
