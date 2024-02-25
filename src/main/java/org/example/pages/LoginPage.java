package org.example.pages;

import org.example.pages.pagecomponents.NavigationBar;
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
    private final NavigationBar navigationBar;
    private final By userNameLocator = By.name("UserID");
    private final By userPasswordLocator = By.name("Password");
    private final By enterButtonLocator = By.xpath("//input[@value='Enter']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.navigationBar = new NavigationBar(driver);
    }

    public WebElement getUserName() {
        return driver.findElement(userNameLocator);
    }

    public WebElement getUserPassword() {
        return driver.findElement(userPasswordLocator);
    }

    public WebElement getEnterButton() {
        return driver.findElement(enterButtonLocator);
    }

    public void loginAsUser(String username, String password) {
        try {
            getUserName().sendKeys(username);
            getUserPassword().sendKeys(password);
            getEnterButton().click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement lettersButtonElement = wait.until(ExpectedConditions.visibilityOf(navigationBar.getLettersButton()));
            boolean displayed = lettersButtonElement.isDisplayed();
            Assert.assertTrue(displayed, "Login was unsuccessful");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}



