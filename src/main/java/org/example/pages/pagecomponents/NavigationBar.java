package org.example.pages.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class NavigationBar {

    private final WebDriver driver;

    private final By lettersButtonLocator = By.id("nav-mail");

    private final By documentsButtonLocator = By.id("nav-docs");

    public NavigationBar(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getLettersButton() {
        return driver.findElement(lettersButtonLocator);
    }

    public WebElement getDocumentsButton() {
        return driver.findElement(documentsButtonLocator);
    }
}
