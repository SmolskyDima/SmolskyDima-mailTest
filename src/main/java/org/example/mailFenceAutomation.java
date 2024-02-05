package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mailFenceAutomation {
    private static final String MAIL_URL = "https://mailfence.com";

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();

        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--lang=ru-RU");
            options.addArguments("--disable-blink-features=AutomationControlled");

            driver.manage().window().maximize();
            driver.get(MAIL_URL);

            WebElement login = driver.findElement(By.xpath("//button[text()='Log in']"));
            login.click();

            WebElement usernameField = driver.findElement(By.name("UserID"));
            WebElement passwordField = driver.findElement(By.name("Password"));

            usernameField.sendKeys(config.getUsername());
            passwordField.sendKeys(config.getPassword());

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            WebElement enterButton = driver.findElement(By.xpath("//input[@value='Enter']"));
            enterButton.click();

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
                isFileLoaded = true;

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

            WebElement mailSubject = driver.findElement(By.id("mailSubject"));
            mailSubject.sendKeys(randomFileName);

            WebElement sendLetter = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//div[text()='Отправить']")));
            sendLetter.click();

            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("//div[@class='listSubject' and contains(text(), '" + randomFileName + "')]")));
            } catch (TimeoutException e) {
                WebElement refreshListOfEmails = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("//div[text()='Обновить']")));
                refreshListOfEmails.click();
            }

            WebElement until = wait.until(ExpectedConditions.presenceOfElementLocated(By
                    .cssSelector("div.listSubject[title='" + randomFileName + "']")));
            until.click();

            WebElement saveButton = wait.until(ExpectedConditions.presenceOfElementLocated(By
                    .xpath("//b[@class='icon-Arrow-down']")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", saveButton);

            WebElement saveInDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//span[@class and text()='Сохранить в документах']")));
            saveInDocument.click();


            WebElement myDocument = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//div[text()='Мои документы']")));
            myDocument.click();

            wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By
                    .xpath("//div[@id='dialBtn_OK']"), "class", "GCSDBRWBMB")));
            WebElement element = driver.findElement(By.xpath("//div[@id='dialBtn_OK']"));
            element.click();

            WebElement docsButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-docs")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", docsButton);
            docsButton.click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }



            WebElement elementToDelete = wait.until(ExpectedConditions.presenceOfElementLocated(By
                    .xpath("//div[contains(@title, '" + randomFileName + "')]")));
            Actions actions = new Actions(driver);
            actions.contextClick(elementToDelete).perform();

            WebElement deletingElement = wait.until(ExpectedConditions.presenceOfElementLocated(By
                    .xpath("//span[text()='Удалить']")));
            deletingElement.click();
            System.out.println("точка остановки");
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}