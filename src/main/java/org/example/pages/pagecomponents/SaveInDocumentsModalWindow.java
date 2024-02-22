package org.example.pages.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SaveInDocumentsModalWindow {

    private final WebDriverWait wait;
    private final By myDocumentLocator = By.xpath("//div[text()='Мои документы']");
    private final By saveButtonLocator = By.xpath("//div[@id='dialBtn_OK']");

    public SaveInDocumentsModalWindow(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public void clickMyDocuments() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(myDocumentLocator)).click();
    }

    public void clickSaveButtonInModalWindow() {
        wait.until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButtonLocator, "class", "GCSDBRWBMB")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonLocator)).click();
    }
}

