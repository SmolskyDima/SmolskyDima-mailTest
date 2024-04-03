package org.example.driver;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.example.driver.WebDriverWrapper.getDriver;

public class Waiter {

    public static WebDriverWait getWaiter() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(30));
    }

}

