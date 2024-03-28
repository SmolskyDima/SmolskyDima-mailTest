package org.example.pages;

import org.example.pages.pagecomponents.SaveDocumentPopup;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

import static org.example.utils.Logger.getLogger;

public class EmailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final By refreshIncomingLettersLocator = By.xpath("//div[@class = 'icon icon16-Refresh']");
    private static final By newLetterButtonLocator = By.xpath("//div[@id='mailNewBtn']");
    private static final By recipientTextBoxLocator = By.className("GCSDBRWBPL");
    private static final By inputLocator = By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB");
    private static final By attachmentInputLocator = By.cssSelector("input[type='file'][name^='docgwt-uid-']");
    private static final By fileUploadCheckboxLocator = By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']");
    private static final By fileUploadProgressBarLocator = By.xpath("//div[@class='GCSDBRWBCS']");
    private static final By mailSubjectTextLocator = By.id("mailSubject");
    private static final By sendLetterLocator = By.xpath("//div[text()='Отправить']");
    private static final By toggleButtonLocator = By.xpath("//b[@class='icon-Arrow-down']");
    private static final By saveInDocumentLocator = By.xpath("//span[@class and text()='Сохранить в документах']");
    private final String receivedElementLocator = "//div[@class='listSubject' and contains(text(), '%s')]";

    public EmailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public WebElement getRefreshIncomingLettersButton() {
        return driver.findElement(refreshIncomingLettersLocator);
    }

    public WebElement getNewLetterButton() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(newLetterButtonLocator));
    }

    public WebElement getRecipientTextBox() {
        return driver.findElement(recipientTextBoxLocator);
    }

    public WebElement getInputButton() {
        return driver.findElement(inputLocator);
    }

    public WebElement getAttachmentInputButton() {
        return driver.findElement(attachmentInputLocator);
    }

    public WebElement getFileUploadCheckbox() {
        return driver.findElement(fileUploadCheckboxLocator);
    }

    public WebElement getFileUploadProgressBar() {
        return driver.findElement(fileUploadProgressBarLocator);
    }

    public WebElement getMailSubjectTextInput() {
        return driver.findElement(mailSubjectTextLocator);
    }

    public WebElement getSendLetterButton() {
        return driver.findElement(sendLetterLocator);
    }

    public WebElement getToggleButton() {
        return driver.findElement(toggleButtonLocator);
    }

    public WebElement getSaveInDocumentButton() {
        return driver.findElement(saveInDocumentLocator);
    }

    public WebElement getReceivedElement(String uniqueName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(String.format(receivedElementLocator, uniqueName))));
    }

    public void clickNewLetterButton() {
        getLogger().info("Start method clickNewLetterButton");
        getNewLetterButton().click();
    }

    public void enterRecipientEmail(String recipient) {
        getLogger().info("Start method enterRecipientEmail");
        getRecipientTextBox().sendKeys(recipient, Keys.ENTER);
    }

    public void attachFileToEmail(Path filePath) {
        getLogger().info("Start method attachFileToEmail");
        getInputButton().click();
        String string = filePath.toAbsolutePath().toString();
        WebElement attachmentInput = getAttachmentInputButton();
        attachmentInput.sendKeys(string);

    }

    public void verifyThatFileIsLoaded() {
        getLogger().info("Start method verifyThatFileIsLoaded");
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
        getLogger().info("Start method setEmailSubject");
        getMailSubjectTextInput().sendKeys(emailSubject);
    }

    public void clickSendLetterButton() {
        getLogger().info("Start method clickSendLetterButton");
        getSendLetterButton().click();
    }

    public void waitUntilEmailReceived(String uniqueName) {
        getLogger().info("Start method waitUntilEmailReceived");
        boolean elementFound = false;
        while (!elementFound) {
            try {
                getReceivedElement(uniqueName).click();
                elementFound = true;
            } catch (TimeoutException e) {
                try {
                    driver.findElement(refreshIncomingLettersLocator).click();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void clickSaveDocumentButton() {
        getLogger().info("Start method clickSaveDocumentButton");
        WebElement toggleButton = getToggleButton();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toggleButton);
        getSaveInDocumentButton().click();
    }

    public SaveDocumentPopup getSaveDocumentPopup() {
        return new SaveDocumentPopup(driver);
    }
}
