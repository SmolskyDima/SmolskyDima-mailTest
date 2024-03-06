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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Duration;

import static org.example.utils.ScreenshotsManager.takeScreenshot;
import static org.example.utils.ScreenshotsManager.takeSource;
import static org.example.utils.UserManager.getUserById;


public class MailFenceAutomationTest {

    private WebDriver driver;
    Path filePath = TemporaryFileCreator.createTempFile();
    String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
    private static final String MAIL_URL = "https://mailfence.com/sw?type=L&state=0&lf=mailfence";
    User user = getUserById("testUser");
    private static final Logger logger = LogManager.getLogger(MailFenceAutomationTest.class);

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
        logger.info("Start test method");
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Start method loginAsUser");
        loginPage.loginAsUser(user.getName(), user.getPassword());
        EmailPage emailPage = new EmailPage(driver);
        logger.info("Start method clickNewLetterButton");
        emailPage.clickNewLetterButton();
        logger.info("Start method enterRecipientEmail");
        emailPage.enterRecipientEmail(user.getEmail());
        logger.info("Start method attachFileToEmail");
        emailPage.attachFileToEmail(filePath);
        logger.info("Start method verifyThatFileIsLoaded");
        emailPage.verifyThatFileIsLoaded();
        logger.info("Start method setEmailSubject");
        emailPage.setEmailSubject(subjectOfEmail);
        logger.info("Start method clickSendLetterButton");
        emailPage.clickSendLetterButton();
        logger.info("Start method waitUntilEmailReceived");
        emailPage.waitUntilEmailReceived(subjectOfEmail);
        logger.info("Start method clickSaveDocumentButton");
        emailPage.clickSaveDocumentButton();
        emailPage.getSaveDocumentPopup().clickMyDocuments();
        emailPage.getSaveDocumentPopup().clickSaveButton();
        DocumentsPage documentsPage = new DocumentsPage(driver);
        logger.info("Start method clickDocumentsButton");
        documentsPage.clickDocumentsButton();
        logger.info("Start method sleepForTwoSeconds");
        documentsPage.sleepForTwoSeconds();
        logger.info("Start method deleteEmailFromDocumentsWithRightClick");
        documentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        logger.info("Start method openTrash");
        documentsPage.openTrash();
        logger.info("Start method assertElementIsPresentInTrash");
        documentsPage.assertElementIsPresentInTrash(subjectOfEmail);
        logger.info("End test method");
    }

    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        if (!result.isSuccess()) {
            takeScreenshot(driver);
            takeSource(driver);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}