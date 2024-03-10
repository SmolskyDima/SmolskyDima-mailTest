import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.User;
import org.example.utils.TemporaryFileCreator;
import org.example.pages.DocumentsPage;
import org.example.pages.EmailPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.time.Duration;

import static org.example.utils.ScreenshotsManager.takeScreenshot;
import static org.example.utils.ScreenshotsManager.takeSource;
import static org.example.utils.UserManager.getUserById;


public class MailFenceAutomationTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String MAIL_URL = "https://mailfence.com/sw?type=L&state=0&lf=mailfence";
    private static final Logger logger = LogManager.getLogger(MailFenceAutomationTest.class);

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get(MAIL_URL);
        driverThreadLocal.set(driver);
    }

    @Test
    public void mailFenceAutomationTest() {
        logger.info("Start test method");
        WebDriver driver = driverThreadLocal.get();
        User user = getUserById("testUser");
        Path filePath = TemporaryFileCreator.createTempFile();
        String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsUser(user.getName(), user.getPassword());
        EmailPage emailPage = new EmailPage(driver);
        emailPage.clickNewLetterButton();
        emailPage.enterRecipientEmail(user.getEmail());
        emailPage.attachFileToEmail(filePath);
        emailPage.verifyThatFileIsLoaded();
        emailPage.setEmailSubject(subjectOfEmail);
        emailPage.clickSendLetterButton();
        emailPage.waitUntilEmailReceived(subjectOfEmail);
        emailPage.clickSaveDocumentButton();
        emailPage.getSaveDocumentPopup().clickMyDocuments();
        emailPage.getSaveDocumentPopup().clickSaveButton();
        DocumentsPage documentsPage = new DocumentsPage(driver);
        documentsPage.clickDocumentsButton();
        documentsPage.sleepForTwoSeconds();
        documentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        documentsPage.openTrash();
        documentsPage.assertElementIsPresentInTrash(subjectOfEmail);
        logger.info("End test method");
    }

    @Test
    public void mailFenceAutomationTest2() {
        logger.info("Start test method2");
        WebDriver driver = driverThreadLocal.get();
        User user = getUserById("testUser");
        Path filePath = TemporaryFileCreator.createTempFile();
        String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
        LoginPage loginPage = new LoginPage(driver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        loginPage.loginAsUser(user.getName(), user.getPassword());
        EmailPage emailPage = new EmailPage(driver);
        emailPage.clickNewLetterButton();
        emailPage.enterRecipientEmail(user.getEmail());
        emailPage.attachFileToEmail(filePath);
        emailPage.verifyThatFileIsLoaded();
        emailPage.setEmailSubject(subjectOfEmail);
        emailPage.clickSendLetterButton();
        emailPage.waitUntilEmailReceived(subjectOfEmail);
        emailPage.clickSaveDocumentButton();
        emailPage.getSaveDocumentPopup().clickMyDocuments();
        emailPage.getSaveDocumentPopup().clickSaveButton();
        DocumentsPage documentsPage = new DocumentsPage(driver);
        documentsPage.clickDocumentsButton();
        documentsPage.sleepForTwoSeconds();
        documentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        documentsPage.openTrash();
        documentsPage.assertElementIsPresentInTrash(subjectOfEmail);
        logger.info("End test method2");
    }
    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            if (!result.isSuccess()) {
                takeScreenshot(driver);
                takeSource(driver);
            }
            driver.quit();
        }
    }
}