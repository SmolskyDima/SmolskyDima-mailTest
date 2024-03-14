package org.example.listener;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestListener extends TestListenerAdapter {

    private static final String SCREENSHOTS_DIR = "src/test/java/screenshots/";

    @Override
    public void onTestFailure(ITestResult tr) {
        Object[] parameters = tr.getParameters();
        WebDriver driver = (WebDriver) parameters[0];
        if (driver != null) {
            System.out.println("Test Failed: " + tr.getName());
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            saveScreenshot(screenshot, tr.getName());
        }
    }

    private void saveScreenshot(byte[] screenshot, String testName) {
        File screenshotDir = new File(SCREENSHOTS_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        String screenshotPath = SCREENSHOTS_DIR + testName + ".png";
        try (FileOutputStream fileOutputStream = new FileOutputStream(screenshotPath)) {
            fileOutputStream.write(screenshot);
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }
}
