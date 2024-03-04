package org.example.pages.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LeftPanel {

    private WebDriver driver;
    private final By trashLocator = By.xpath("//div[@id='doc_tree_trash']");

    public LeftPanel(WebDriver driver) {
        this.driver = driver;
    }

    public  WebElement getTrashButton() {
        return driver.findElement(trashLocator);
    }
}