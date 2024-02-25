package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotsManager {

    private static final String SCREENSHOTS_DIR = "src/test/java/screenshots";

    public static void takeScreenshot(WebDriver driver) {

        String screenshotsDirectory = Paths.get(SCREENSHOTS_DIR).toString();

        Path screenshotsPath = Paths.get(screenshotsDirectory);
        if (!Files.exists(screenshotsPath)) {
            try {
                Files.createDirectories(screenshotsPath);
                System.out.println("Директория для скриншотов создана: " + screenshotsPath);
            } catch (IOException e) {
                System.out.println("Не удалось создать директорию для скриншотов: " + e.getMessage());
                return;
            }
        }
        String screenshotName = "screenshot.png";
        String screenshotPath = Paths.get(screenshotsDirectory, screenshotName).toString();
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            System.out.println("Скриншот сохранен: " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить скриншот: " + e.getMessage());
        }
    }

    public static void takeSource(WebDriver driver) {

        String screenshotsDirectory = Paths.get(SCREENSHOTS_DIR).toString();
        String pageSource = driver.getPageSource();
        String pageSourceName = "page_source.html";
        String pageSourcePath = Paths.get(screenshotsDirectory, pageSourceName).toString();
        try {
            Files.write(Paths.get(pageSourcePath), pageSource.getBytes());
            System.out.println("Исходный код страницы сохранен: " + pageSourcePath);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить исходный код страницы: " + e.getMessage());
        }
    }
}

