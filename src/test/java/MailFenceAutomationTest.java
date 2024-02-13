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
    private static final String LOGIN_BUTTON_LOCATOR = "//button[text()='Log in']";
    private static final String ENTER_BUTTON_LOCATOR = "//input[@value='Enter']";
    private static final String LETTERS_BUTTON_LOCATOR = "nav-mail";
    private static final String CREATE_NEW_LETTER_BUTTON_LOCATOR = "//div[text()='Создать']";
    private static final String NAME_OF_EMAIL_LOCATOR = "//div[@class='GCSDBRWBCQ']";
    private static final String RECIPIENT_TEXT_BOX_LOCATOR = "GCSDBRWBPL";
    private static final String INPUT_LOCATOR = ".GCSDBRWBJSB.GCSDBRWBKSB";
    private static final String INPUT_FROM_COMPUTER_LOCATOR = "input[type='file'][name^='docgwt-uid-']";
    private static final String LOADING_IS_FINISHED_CHECKBOX_LOCATOR = "//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']";
    private static final String PROGRESS_LOADING_BAR_LOCATOR = "//div[@class='GCSDBRWBCS']";
    private static final String MAIL_SUBJECT_TEXT_BOX_LOCATOR = "mailSubject";
    private static final String REFRESH_INCOMING_LETTERS_LOCATOR = "//div[text()='Обновить']";
    private static final String SEND_LETTER_LOCATOR = "//div[text()='Отправить']";
    private static final String TOGGLE_LOCATOR = "//b[@class='icon-Arrow-down']";
    private static final String SAVE_IN_DOCUMENT_LOCATOR = "//span[@class and text()='Сохранить в документах']";
    private static final String MY_DOCUMENT_LOCATOR = "//div[text()='Мои документы']";
    private static final String SAVE_IN_MY_DOCUMENT_LOCATOR = "//div[@id='dialBtn_OK']";
    private static final String DOCUMENTS_BUTTON_LOCATOR = "nav-docs";
    private static final String DELETE_DOCUMENT_LOCATOR = "//span[text()='Удалить']";

    @BeforeClass
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

    }

    @Test
    public void testMailFenceAutomation() {

        driver.get(MAIL_URL);

        WebElement login = driver.findElement(By.xpath(LOGIN_BUTTON_LOCATOR));
        login.click();

        WebElement usernameField = driver.findElement(By.name("UserID"));
        WebElement passwordField = driver.findElement(By.name("Password"));

        usernameField.sendKeys(Config.getUsername());
        passwordField.sendKeys(Config.getPassword());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement enterButton = driver.findElement(By.xpath(ENTER_BUTTON_LOCATOR));
        enterButton.click();

        WebElement letters = driver.findElement(By.id(LETTERS_BUTTON_LOCATOR));
        letters.click();

        WebElement newLetter = driver.findElement(By.xpath(CREATE_NEW_LETTER_BUTTON_LOCATOR));
        newLetter.click();

        WebElement myEmailAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(NAME_OF_EMAIL_LOCATOR)));

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

        WebElement emailTo = driver.findElement(By.className(RECIPIENT_TEXT_BOX_LOCATOR));
        emailTo.sendKeys(extractedEmail, Keys.ENTER);

        WebElement elementInput = driver.findElement(By.cssSelector(INPUT_LOCATOR));
        elementInput.click();

        TemporaryFileManager createTempFile = new TemporaryFileManager();
        String absolutePath = createTempFile.getFilePath().toAbsolutePath().toString();

        WebElement fileInput = driver.findElement(By
                .cssSelector(INPUT_FROM_COMPUTER_LOCATOR));
        fileInput.sendKeys(absolutePath);

        boolean isFileLoaded = false;

        try {
            driver.findElement(By.xpath(LOADING_IS_FINISHED_CHECKBOX_LOCATOR));

        } catch (NoSuchElementException e) {
            if (!isFileLoaded) {

                try {
                    wait.until(ExpectedConditions.attributeToBe(By
                            .xpath(PROGRESS_LOADING_BAR_LOCATOR), "style", "width: 100%; visibility: visible;"));
                } catch (TimeoutException second) {
                    System.out.println("Не удалось подтвердить успешную загрузку файла.");
                }
            }
        }

        WebElement mailSubject = driver.findElement(By.id(MAIL_SUBJECT_TEXT_BOX_LOCATOR));

        mailSubject.sendKeys(createTempFile.getExtractedDigitsFromFile());

        WebElement sendLetter = driver.findElement(By.xpath(SEND_LETTER_LOCATOR));
        sendLetter.click();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//div[@class='listSubject' and contains(text(), '"
                            + createTempFile.getExtractedDigitsFromFile() + "')]")));
        } catch (
                TimeoutException e) {
            WebElement refreshListOfEmails = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath(REFRESH_INCOMING_LETTERS_LOCATOR)));
            refreshListOfEmails.click();
        }

        WebElement until = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .cssSelector("div.listSubject[title='" + createTempFile.getExtractedDigitsFromFile() + "']")));
        until.click();

        WebElement toggleButton = driver.findElement(By.xpath(TOGGLE_LOCATOR));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toggleButton);

        WebElement saveInDocument = driver.findElement(By.xpath(SAVE_IN_DOCUMENT_LOCATOR));
        saveInDocument.click();


        WebElement myDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(MY_DOCUMENT_LOCATOR)));
        myDocument.click();


        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By
                .xpath("//div[@id='dialBtn_OK']"), "class", "GCSDBRWBMB")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(SAVE_IN_MY_DOCUMENT_LOCATOR)));
        saveButton.click();

        WebElement docsButton = driver.findElement(By.id(DOCUMENTS_BUTTON_LOCATOR));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
        docsButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement elementToDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//div[contains(@title, '" + createTempFile.getExtractedDigitsFromFile() + "')]")));
        Actions actions = new Actions(driver);
        actions.contextClick(elementToDelete).perform();

        WebElement deleteFromDocumentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath(DELETE_DOCUMENT_LOCATOR)));
        deleteFromDocumentElement.click();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}