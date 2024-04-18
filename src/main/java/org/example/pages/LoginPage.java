package org.example.pages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.example.elements.Button;
import org.example.elements.Element;
import org.example.elements.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.NoSuchElementException;

import static org.example.driver.Waiter.getWaiter;
import static org.example.utils.Logger.getLogger;

public class LoginPage {
    @Getter
    private static final Input userName = new Input(By.name("UserID"), "User name");
    @Getter
    private static final Input userPassword = new Input(By.name("Password"), "Password");
    @Getter
    private static final Button enterButton = new Button(By.xpath("//input[@value='Enter']"), "Enter");
    @Getter
    private static final Element spinner = new Element(By.cssSelector(".progress"), "Spinner");

    @Step("Start method loginAsUser")
    public static void loginAsUser(String username, String password) {
        try {
            userName.sendKeys(username);
            userPassword.sendKeys(password);
            enterButton.click();

            Assert.assertTrue(waitForSpinnerToDisappear(), "Page is not loaded after login");
        } catch (NoSuchElementException e) {
            getLogger().error(" ", e);
        }
    }
    @Step("Waiting for the start page to load - spinner has to disappear")
    public static boolean waitForSpinnerToDisappear() {
        try {
            getWaiter().until(ExpectedConditions.invisibilityOf(spinner.getElement()));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}