import org.example.utils.FileManager;
import org.example.utils.TemporaryFileCreator;
import org.example.utils.Config;
import org.example.pages.DocumentsPage;
import org.example.pages.EmailPage;
import org.example.pages.FileActionsPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Duration;

import static org.example.utils.ScreenshotsManager.takeScreenshot;


public class MailFenceAutomationTest {

    private WebDriver driver;
    Path filePath = new TemporaryFileCreator().createTempFile();
    String subjectOfEmail = FileManager.extractFileName(filePath);
    private static final String MAIL_URL = "https://mailfence.com";


    @BeforeClass
    public void setUp() {


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get(MAIL_URL);

    }

    @Test
    public void mailFenceAutomationTest() {

        LoginPage loginPage = new LoginPage(driver);
        EmailPage emailPage = new EmailPage(driver);
        DocumentsPage documentsPage = new DocumentsPage(driver);
        FileActionsPage fileActionsPage = new FileActionsPage(driver);
        loginPage.clickLoginButton();
        loginPage.loginAsUser(Config.getUsername(), Config.getPassword());
        loginPage.verifyLogin();
        emailPage.clickLettersButton();
        emailPage.clickNewLetterButton();
        emailPage.enterRecipientEmail(Config.getEmail());
        emailPage.attachFileToEmail(filePath);
        emailPage.verifyFileLoading();
        emailPage.setEmailSubject(subjectOfEmail);
        emailPage.clickSendLetterButton();
        documentsPage.waitUntilEmailReceived(subjectOfEmail);
        fileActionsPage.clickAttachedFileInDocument();
        fileActionsPage.clickMyDocuments();
        fileActionsPage.waitForSaveButtonToBeClickable();
        fileActionsPage.clickSaveButton();
        documentsPage.clickDocumentsButton();
        documentsPage.sleepForTwoSeconds();
        documentsPage.deleteEmailFromDocument(subjectOfEmail);
    }

    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(driver);
        }

    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}