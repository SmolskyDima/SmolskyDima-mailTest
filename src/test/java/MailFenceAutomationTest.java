import org.example.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailFenceAutomationTest {

    private WebDriver driver;
    private static final String MAIL_URL = "https://mailfence.com";
    TemporaryFileCreator fileCreator = new TemporaryFileCreator();

    @BeforeClass
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testMailFenceAutomation() {

        driver.get(MAIL_URL);

        WebElement login = driver.findElement(By.xpath("//button[text()='Log in']"));
        login.click();

        WebElement usernameField = driver.findElement(By.name("UserID"));
        WebElement passwordField = driver.findElement(By.name("Password"));

        usernameField.sendKeys(Config.getUsername());
        passwordField.sendKeys(Config.getPassword());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement enterButton = driver.findElement(By.xpath("//input[@value='Enter']"));
        enterButton.click();

        WebElement letters = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-mail")));
        letters.click();

        WebElement newLetter = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Создать']")));
        newLetter.click();

        WebElement myEmailAddress = wait.until(ExpectedConditions.
                visibilityOfElementLocated(By.xpath("//div[@class='GCSDBRWBCQ']")));

        String contentToCopy = myEmailAddress.getText();

        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(contentToCopy);

        String extractedEmail = "";
        if (matcher.find()) {
            extractedEmail = matcher.group();
        } else {
            System.out.println("Почтовый адрес не найден.");
        }

        WebElement emailTo = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("GCSDBRWBPL")));
        emailTo.sendKeys(extractedEmail, Keys.ENTER);

        WebElement elementInput = driver.findElement(By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB"));
        elementInput.click();

        String absolutePath = fileCreator.getFilePath().toAbsolutePath().toString();

        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .cssSelector("input[type='file'][name^='docgwt-uid-']")));
        fileInput.sendKeys(absolutePath);

        boolean isFileLoaded = false;

        try {
            WebElement loadingIsFinished = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']")));

        } catch (TimeoutException e) {
            if (!isFileLoaded) {

                try {
                    Boolean style = wait.until(ExpectedConditions.attributeToBe(
                            By.xpath("//div[@class='GCSDBRWBCS']"), "style", "width: 100%; visibility: visible;"));
                } catch (TimeoutException second) {
                    System.out.println("Не удалось подтвердить успешную загрузку файла.");
                }
            }
        }

        WebElement mailSubject = driver.findElement(By.id("mailSubject"));
        mailSubject.sendKeys(fileCreator.getExtractedDigitsFromFile());

        WebElement sendLetter = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//div[text()='Отправить']")));
        sendLetter.click();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//div[@class='listSubject' and contains(text(), '" + fileCreator.getExtractedDigitsFromFile() + "')]")));
        } catch (TimeoutException e) {
            WebElement refreshListOfEmails = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//div[text()='Обновить']")));
            refreshListOfEmails.click();
        }

        WebElement until = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .cssSelector("div.listSubject[title='" + fileCreator.getExtractedDigitsFromFile() + "']")));
        until.click();

        WebElement saveButton = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//b[@class='icon-Arrow-down']")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", saveButton);

        WebElement saveInDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//span[@class and text()='Сохранить в документах']")));
        saveInDocument.click();


        WebElement myDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//div[text()='Мои документы']")));
        myDocument.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By
                .xpath("//div[@id='dialBtn_OK']"), "class", "GCSDBRWBMB")));
        WebElement element = driver.findElement(By.xpath("//div[@id='dialBtn_OK']"));
        element.click();

        WebElement docsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-docs")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
        docsButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        WebElement elementToDelete = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//div[contains(@title, '" + fileCreator.getExtractedDigitsFromFile() + "')]")));
        Actions actions = new Actions(driver);
        actions.contextClick(elementToDelete).perform();

        WebElement deletingElement = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//span[text()='Удалить']")));
        deletingElement.click();
        driver.quit();

    }
}