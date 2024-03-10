package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenshotsManager {

    private static final String SCREENSHOTS_DIR = "src/test/java/screenshots";

    public synchronized static void takeScreenshot(WebDriver driver) {
        long threadId = Thread.currentThread().getId();
        String screenshotName = "screenshot_" + threadId + ".png";
        saveScreenshot(driver, screenshotName);
    }

    private static void saveScreenshot(WebDriver driver, String screenshotName) {
        synchronized (ScreenshotsManager.class) {
            String screenshotsDirectory = Paths.get(SCREENSHOTS_DIR).toString();
            String screenshotPath = Paths.get(screenshotsDirectory, screenshotName).toString();
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

            try {
                FileUtils.copyFile(srcFile, new File(screenshotPath));
                System.out.println("Screenshot saved: " + screenshotPath);
            } catch (IOException e) {
                System.out.println("Screenshot didn't saved: " + e.getMessage());
            }
        }
    }

    public static void takeSource(WebDriver driver) {
        long threadId = Thread.currentThread().getId();
        String pageSourceName = "page_source_" + threadId + ".html";
        savePageSource(driver, pageSourceName);
    }

    private static void savePageSource(WebDriver driver, String pageSourceName) {
        synchronized (ScreenshotsManager.class) {
            String screenshotsDirectory = Paths.get(SCREENSHOTS_DIR).toString();
            String pageSource = driver.getPageSource();
            String pageSourcePath = Paths.get(screenshotsDirectory, pageSourceName).toString();
            try {
                Files.write(Paths.get(pageSourcePath), pageSource.getBytes());
                System.out.println("Page source code saved: " + pageSourcePath);
            } catch (IOException e) {
                System.out.println("Could not save the page source code: " + e.getMessage());
            }
        }
    }
}

