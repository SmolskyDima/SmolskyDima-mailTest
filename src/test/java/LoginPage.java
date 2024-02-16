import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.NoSuchElementException;

public class LoginPage {
    private final WebDriver driver;
    private final By loginButtonLocator = By.xpath("//button[text()='Log in']");
    private final By userNameLocator = By.name("UserID");
    private final By passwordLocator = By.name("Password");
    private final By enterButtonLocator = By.xpath("//input[@value='Enter']");
    private final By lettersButtonLocator = By.id("nav-mail");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLoginButton() {
        driver.findElement(loginButtonLocator).click();
    }

    public void loginAsUser(String username, String password) {
        try {
            driver.findElement(userNameLocator).sendKeys(username);
            driver.findElement(passwordLocator).sendKeys(password);
            driver.findElement(enterButtonLocator).click();
        } catch(NoSuchElementException e ) {
            e.printStackTrace();
        }


    }

    public boolean isLoggedIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement lettersButtonElement = wait
                .until(ExpectedConditions.visibilityOfElementLocated(lettersButtonLocator));
        return lettersButtonElement.isDisplayed();
    }

    public void verifyLogin() {
        Assert.assertTrue(isLoggedIn(), "Login was unsuccessful");
    }

}



