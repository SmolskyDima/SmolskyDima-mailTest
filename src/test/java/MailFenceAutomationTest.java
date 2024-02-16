import org.example.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class MailFenceAutomationTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private EmailPage emailPage;
    private DocumentsPage documentsPage;
    private FileActionsPage fileActionsPage;
    String filePath = new TemporaryFileCreator().createTempFile();
    String extractedDigits = FileManager.extractRandomFileName(filePath);
    private static final String MAIL_URL = "https://mailfence.com";



    @BeforeClass
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        //пришлось инициализировать POM классы здесь, потому что driver здесь инициализируется - в полях передаётся null
        loginPage = new LoginPage(driver);
        emailPage = new EmailPage(driver);
        documentsPage = new DocumentsPage(driver);
        fileActionsPage = new FileActionsPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get(MAIL_URL);

    }

    @Test
    public void mailFenceAutomationTest() {

        loginPage.clickLoginButton();
        loginPage.loginAsUser(Config.getUsername(), Config.getPassword());
        loginPage.verifyLogin();
        emailPage.clickLettersButton();
        emailPage.clickNewLetterButton();
        emailPage.enterRecipientEmail();
        emailPage.clickInputLocator();
        emailPage.attachFileToEmail(filePath);
        emailPage.verifyFileLoading();
        emailPage.setEmailSubject(extractedDigits);
        emailPage.sendEmail();
        documentsPage.checkGettingEmail(extractedDigits);
        fileActionsPage.clickToggleButton();
        fileActionsPage.saveInDocument();
        fileActionsPage.clickMyDocuments();
        fileActionsPage.waitForSaveButtonToBeClickable();
        fileActionsPage.clickSaveButton();
        documentsPage.clickDocumentsButton();
        documentsPage.sleepForTwoSeconds();
        documentsPage.deleteDocument();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}