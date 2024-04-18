package org.example.elements;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.example.driver.WebDriverWrapper.getDriver;

public class Element {

    @Getter
    protected By locator;
    protected String elementName;

    public Element(By locator, String elementName) {
        this.locator = locator;
        this.elementName = elementName;
    }


    public WebElement getElement() {
        return getDriver().findElement(locator);
    }

    public void click() {
        getElement().click();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "locator=" + locator +
                ", elementName='" + elementName + '\'' +
                '}';
    }
}
