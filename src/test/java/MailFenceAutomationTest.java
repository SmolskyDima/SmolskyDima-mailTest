import org.example.entity.User;
import org.example.pages.DocumentsPage;
import org.example.pages.EmailPage;
import org.example.pages.LoginPage;
import org.example.utils.TemporaryFileCreator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.example.driver.WebDriverWrapper.getDriver;
import static org.example.driver.WebDriverWrapper.quitDriver;
import static org.example.pages.DocumentsPage.sleepForTwoSeconds;
import static org.example.utils.UserManager.getUserById;

public class MailFenceAutomationTest {

    private static final String MAIL_URL = "https://mailfence.com/sw?type=L&state=0&lf=mailfence";

    @BeforeMethod
    public void setUp() {
        getDriver().get(MAIL_URL);
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
        sleepForTwoSeconds();
        DocumentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        DocumentsPage.openTrash();
        DocumentsPage.assertElementIsPresentInTrash(subjectOfEmail);
    }

    @Test
    public void mailFenceAutomationTestDuplicate() {
        User user = getUserById("testUser");
        Path filePath = TemporaryFileCreator.createTempFile();
        String subjectOfEmail = TemporaryFileCreator.extractFileName(filePath);
        sleepForTwoSeconds();
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
        sleepForTwoSeconds();
        DocumentsPage.deleteEmailFromDocumentsWithRightClick(subjectOfEmail);
        DocumentsPage.openTrash();
        DocumentsPage.assertElementIsPresentInTrash(subjectOfEmail);
    }

    @AfterMethod
    public void closeWebDriver() {
        quitDriver();
    }

}