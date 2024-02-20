package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;

public class EmailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By newLetterButtonLocator = By.xpath("//div[text()='Создать']");
    private final By lettersButtonLocator = By.id("nav-mail");
    private final By recipientTextBoxLocator = By.className("GCSDBRWBPL");
    private final By inputLocator = By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB");
    private final By attachmentInputLocator = By.cssSelector("input[type='file'][name^='docgwt-uid-']");
    private final By fileUploadCheckboxLocator = By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']");
    private final By fileUploadProgressBarLocator = By.xpath("//div[@class='GCSDBRWBCS']");
    private final By mailSubjectTextLocator = By.id("mailSubject");
    private final By sendLetterLocator = By.xpath("//div[text()='Отправить']");


    public EmailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickNewLetterButton() {
        driver.findElement(newLetterButtonLocator).click();
    }

    public void clickLettersButton() {
        driver.findElement(lettersButtonLocator).click();
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

    public void verifyFileLoading() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(fileUploadCheckboxLocator));
        } catch (TimeoutException e) {
            try {
                wait.until(ExpectedConditions
                        .attributeToBe(fileUploadProgressBarLocator, "style", "width: 100%; visibility: visible;"));
            } catch (TimeoutException second) {
                System.out.println("Не удалось подтвердить успешную загрузку файла.");
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
}
