import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By newLetterButtonLocator = By.xpath("//div[text()='Создать']");
    private final By lettersButtonLocator = By.id("nav-mail");
    private final By nameOfEmailLocator = By.xpath("//div[@class='GCSDBRWBCQ']");
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


    public String extractEmailFromText(String text) {
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            System.out.println("Email not found");
            return "";
        }
    }

    public void enterRecipientEmail() {
        String extractedEmail = extractEmailFromText(wait
                .until(ExpectedConditions.visibilityOfElementLocated(nameOfEmailLocator)).getText());
        WebElement emailTo = driver.findElement(recipientTextBoxLocator);
        emailTo.sendKeys(extractedEmail, Keys.ENTER);
    }

    public void clickInputLocator() {
        driver.findElement(inputLocator).click();
    }

    public void attachFileToEmail(String filePath) {
        WebElement attachmentInput = driver.findElement(attachmentInputLocator);
        attachmentInput.sendKeys(filePath);
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

    public void sendEmail() {
        driver.findElement(sendLetterLocator).click();
    }
}
