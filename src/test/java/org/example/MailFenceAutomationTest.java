package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailFenceAutomationTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--lang=ru-RU");
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testMailFenceAutomation() {

        login();
        createNewEmail();
    }

    private void login() {

        driver.get("https://mailfence.com");

        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Log in']"));
        loginButton.click();

        WebElement usernameField = driver.findElement(By.name("UserID"));
        WebElement passwordField = driver.findElement(By.name("Password"));

        usernameField.sendKeys(config.getUsername());
        passwordField.sendKeys(config.getPassword());

        WebElement enterButton = driver.findElement(By.xpath("//input[@value='Enter']"));
        enterButton.click();
    }

    private void createNewEmail() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement letters = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-mail")));
        letters.click();

        WebElement newLetter = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Создать']")));
        newLetter.click();

        WebElement myEmailAddress = wait.until(ExpectedConditions.
                visibilityOfElementLocated(By.xpath("//div[@class='GCSDBRWBCQ']")));

        String contentToCopy = myEmailAddress.getText();

        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(contentToCopy);

        String extractedEmail = "";
        if (matcher.find()) {
            extractedEmail = matcher.group();
        } else {
            System.out.println("Почтовый адрес не найден.");
        }

        WebElement emailTo = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("GCSDBRWBPL")));
        emailTo.sendKeys(extractedEmail, Keys.ENTER);

        WebElement elementInput = driver.findElement(By.cssSelector(".GCSDBRWBJSB.GCSDBRWBKSB"));
        elementInput.click();

        Path filePath;
        String randomFileName;

        try {
            filePath = Files.createTempFile(null, ".txt");
            FileWriter writer = new FileWriter(filePath.toFile());
            writer.write("Hello");
            writer.close();

            String fileName = filePath.getFileName().toString();

            Pattern secondPattern = Pattern.compile("\\d+");
            Matcher secondMatcher = secondPattern.matcher(fileName);
            StringBuilder result = new StringBuilder();

            while (secondMatcher.find()) {
                result.append(secondMatcher.group());
            }

            randomFileName = result.toString();

        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл.", e);
        }

        String absolutPath = filePath.toAbsolutePath().toString();

        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .cssSelector("input[type='file'][name^='docgwt-uid-']")));
        fileInput.sendKeys(absolutPath);

        boolean isFileLoaded = false;

        try {
            WebElement loadingIsFinished = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='GCSDBRWBCSB GCSDBRWBN widgetActive']")));

        } catch (TimeoutException e) {
            if (!isFileLoaded) {

                try {
                    Boolean style = wait.until(ExpectedConditions.attributeToBe(
                            By.xpath("//div[@class='GCSDBRWBCS']"), "style", "width: 100%; visibility: visible;"));
                } catch (TimeoutException second) {
                    System.out.println("Не удалось подтвердить успешную загрузку файла.");
                }
            }
        }

    }
}
