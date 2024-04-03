package org.example.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.example.driver.WebDriverWrapper.getDriver;

public class Element {

    protected By locator;

    public Element(By locator) {
        this.locator = locator;
    }

    public WebElement getElement() {
        return getDriver().findElement(locator);
    }

    public By getLocator() {
        return locator;
    }
}
