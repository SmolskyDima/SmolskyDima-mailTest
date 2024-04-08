package org.example.driver;

import org.example.elements.Element;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.example.driver.WebDriverWrapper.getDriver;

public class Waiter {

    public static WebDriverWait getWaiter() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    public static void waitForVisibility(Element element) {
         getWaiter().until(ExpectedConditions.visibilityOfElementLocated(element.getLocator()));
    }
}

