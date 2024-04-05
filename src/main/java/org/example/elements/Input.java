package org.example.elements;

import org.openqa.selenium.By;

public class Input extends Element {

    public Input(By locator, String elementName) {
        super(locator, elementName);
    }

    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }
}
