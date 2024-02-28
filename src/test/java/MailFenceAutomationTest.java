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
        emailPage.saveDocumentPopup().clickSaveButtonInModalWindow();
        emailPage.saveDocumentPopup().clickMyDocuments();
        DocumentsPage documentsPage = new DocumentsPage(driver);
        documentsPage.clickDocumentsButton();
        documentsPage.sleepForTwoSeconds();
        documentsPage.deleteEmailFromDocumentWithRightClick(subjectOfEmail);
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