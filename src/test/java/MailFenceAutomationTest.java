import org.example.entity.User;
import org.example.listener.TestListener;
import org.example.pages.DocumentsPage;
import org.example.pages.EmailPage;
import org.example.pages.LoginPage;
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
    public void setUp(ITestContext context) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get(MAIL_URL);
        context.setAttribute("WebDriver", driver);
    }

    @Test
    public void mailFenceAutomationTest(ITestContext context) {
        WebDriver driver = (WebDriver) context.getAttribute("WebDriver");
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
    }

    @Test
    public void mailFenceAutomationTest2(ITestContext context) {
        WebDriver driver = (WebDriver) context.getAttribute("WebDriver");
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
    }

    @AfterMethod
    public void closeWebDriver(ITestContext context) {
        WebDriver driver = (WebDriver) context.getAttribute("WebDriver");
        if (driver != null) {
            driver.quit();
        }
    }
}