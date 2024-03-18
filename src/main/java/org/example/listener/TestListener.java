package org.example.listener;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.example.utils.Logger.getLogger;

public class TestListener extends TestListenerAdapter {

    private final String SCREENSHOTS_DIR = "src/test/java/screenshots/";

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
        getLogger().info("Test failed: " + result.getName());

        if (driver instanceof TakesScreenshot) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                saveScreenshot(screenshot, result.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveScreenshot(File screenshot, String testName) throws IOException {
        String fileName = testName + "_" + Thread.currentThread().getName() + ".png";
        Path screenshotPath = Paths.get(SCREENSHOTS_DIR + fileName);
        Files.createDirectories(screenshotPath.getParent());
        Files.copy(screenshot.toPath(), screenshotPath);
        getLogger().info("Screenshot saved: " + screenshotPath);
    }

    @Override
    public void onTestStart(ITestResult result) {
        getLogger().info("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getLogger().info("Test Passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getLogger().info("Test Skipped: " + result.getName());
    }
}
