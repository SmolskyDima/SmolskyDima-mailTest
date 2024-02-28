package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.NoSuchElementException;

public class LoginPage {
    private final WebDriver driver;
    private final EmailPage emailPage;
    private final By userNameLocator = By.name("UserID");
    private final By userPasswordLocator = By.name("Password");
    private final By enterButtonLocator = By.xpath("//input[@value='Enter']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.emailPage = new EmailPage(driver);
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
            getUserNameInputField().sendKeys(username);
            getUserPasswordInputField().sendKeys(password);
            getEnterButton().click();

            Assert.assertTrue(emailPage.isLoginSuccessful(), "Page is not loaded after login");

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}