import org.example.entity.User;
import org.example.listener.TestListener;
import org.example.utils.TemporaryFileCreator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Duration;

import static org.example.utils.UserManager.getUserById;

@Listeners(TestListener.class)
public class MailFenceAutomationTest {

    private static final String MAIL_URL = "https://mailfence.com/sw?type=L&state=0&lf=mailfence";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get(MAIL_URL);
    }

    @Test
    public void mailFenceAutomationTest() {
        User user = getUserById("testUser");
        Path filePath = TemporaryFileCreator.createTempFile();
        String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
        LoginPage.loginAsUser(user.getName(), user.getPassword());
        EmailPage.clickNewLetterButton();
        EmailPage.enterRecipientEmail(user.getEmail());
        EmailPage.attachFileToEmail(filePath);
        EmailPage.verifyThatFileIsLoaded();
        EmailPage.setEmailSubject(subjectOfEmail);
        EmailPage.clickSendLetterButton();
        EmailPage.waitUntilEmailReceived(subjectOfEmail);
        EmailPage.clickSaveDocumentButton();
        EmailPage.getSaveDocumentPopup().clickMyDocuments();
        EmailPage.getSaveDocumentPopup().clickSaveButton();
        DocumentsPage.clickDocumentsButton();
        DocumentsPage.sleepForTwoSeconds();
        DocumentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        DocumentsPage.openTrash();
        DocumentsPage.assertElementIsPresentInTrash(subjectOfEmail);
    }

    @Test
    public void mailFenceAutomationTest2() {
        User user = getUserById("testUser");
        Path filePath = TemporaryFileCreator.createTempFile();
        String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
        LoginPage.loginAsUser(user.getName(), user.getPassword());
        EmailPage.clickNewLetterButton();
        EmailPage.enterRecipientEmail(user.getEmail());
        EmailPage.attachFileToEmail(filePath);
        EmailPage.verifyThatFileIsLoaded();
        EmailPage.setEmailSubject(subjectOfEmail);
        EmailPage.clickSendLetterButton();
        EmailPage.waitUntilEmailReceived(subjectOfEmail);
        EmailPage.clickSaveDocumentButton();
        EmailPage.getSaveDocumentPopup().clickMyDocuments();
        EmailPage.getSaveDocumentPopup().clickSaveButton();
        DocumentsPage.clickDocumentsButton();
        DocumentsPage.sleepForTwoSeconds();
        DocumentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        DocumentsPage.openTrash();
        DocumentsPage.assertElementIsPresentInTrash(subjectOfEmail);
    }

    @AfterMethod
    public void closeWebDriver(ITestContext context) {
        WebDriver driver = (WebDriver) context.getAttribute("WebDriver");
        if (driver != null) {
            driver.quit();
        }
    }
}