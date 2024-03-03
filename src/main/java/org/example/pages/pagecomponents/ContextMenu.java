package org.example.pages.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ContextMenu {

    private final WebDriver driver;
    private final By deleteElementLocator = By.xpath("//span[text()='Удалить']");

    public ContextMenu(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getDeleteElementButton() {
        return driver.findElement(deleteElementLocator);
    }
}
