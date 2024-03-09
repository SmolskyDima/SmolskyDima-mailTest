package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class LoginPage {
    private final WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private final By userNameLocator = By.name("UserID");
    private final By userPasswordLocator = By.name("Password");
    private final By enterButtonLocator = By.xpath("//input[@value='Enter']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getUserNameInputField() {
        return driver.findElement(userNameLocator);
    }

    public WebElement getUserPasswordInputField() {
        return driver.findElement(userPasswordLocator);
    }

    public WebElement getEnterButton() {
        return driver.findElement(enterButtonLocator);
    }

    public void loginAsUser(String username, String password) {
        try {
            logger.info("Start method loginAsUser");
            getUserNameInputField().sendKeys(username);
            getUserPasswordInputField().sendKeys(password);
            getEnterButton().click();
//            EmailPage emailPage = new EmailPage(driver);
//            Assert.assertTrue(emailPage.isPageOpened(), "Page is not loaded after login");

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}