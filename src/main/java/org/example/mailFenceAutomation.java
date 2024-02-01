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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mailFenceAutomation {


    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ru-RU");
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://mailfence.com");

        WebElement login = driver.findElement(By.xpath("//button[text()='Log in']"));
        login.click();

        WebElement usernameField = driver.findElement(By.name("UserID"));
        WebElement passwordField = driver.findElement(By.name("Password"));

        usernameField.sendKeys(Config.getUsername());
        passwordField.sendKeys(Config.getPassword());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        WebElement enterButton = driver.findElement(By.xpath("//input[@value='Enter']"));
        enterButton.click();

        wait.until(ExpectedConditions.urlContains("https://mailfence.com"));


        WebElement letters = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-mail")));
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

        Path filePath = null;
        String randomFileName="";
        try {
            randomFileName = UUID.randomUUID().toString();
            filePath = Files.createTempFile(randomFileName, ".txt");
            FileWriter writer = new FileWriter(filePath.toFile());
            writer.write("Hello");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл.", e);
        }

        String absolutPath = filePath.toAbsolutePath().toString();

        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .cssSelector("input[type='file'][name^='docgwt-uid-']")));
        fileInput.sendKeys(absolutPath);

        Boolean style = wait
                .until(ExpectedConditions.attributeToBe(By
                        .xpath("//div[@class='GCSDBRWBCS']"), "style", "width: 100%; visibility: visible;"));

        driver.findElement(By.id("mailSubject")).sendKeys(randomFileName);

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
                .xpath("//div[contains(@title, '" + randomFileName  +  "')]")));
        Actions actions = new Actions(driver);
        actions.contextClick(elementToDelete).perform();

        WebElement deletingElement = wait.until(ExpectedConditions.presenceOfElementLocated(By
                .xpath("//span[text()='Удалить']")));
        deletingElement.click();

        driver.quit();
    }
}