package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;

public class LoginPage {
    private final WebDriver driver;
    private final By loginButtonLocator = By.xpath("//button[text()='Log in']");
    private final By userNameLocator = By.name("UserID");
    private final By userPasswordLocator = By.name("Password");
    private final By enterButtonLocator = By.xpath("//input[@value='Enter']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLoginButton() {
        driver.findElement(loginButtonLocator).click();
    }

    public void loginAsUser(String username, String password) {
        try {
            driver.findElement(userNameLocator).sendKeys(username);
            driver.findElement(userPasswordLocator).sendKeys(password);
            driver.findElement(enterButtonLocator).click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}



