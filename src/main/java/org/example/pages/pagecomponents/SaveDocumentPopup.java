package org.example.pages.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SaveDocumentPopup {

    private final WebDriverWait wait;
    private final By myDocumentLocator = By.xpath("//div[text()='Мои документы']");
    private final By saveButtonLocator = By.xpath("//div[@id='dialBtn_OK']");

    public SaveDocumentPopup(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public WebElement getMyDocumentButton(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(myDocumentLocator));
    }

    public void clickMyDocuments() {
        getMyDocumentButton().click();
    }

    public void clickSaveButtonInModalWindow() {
        wait.until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButtonLocator, "class", "GCSDBRWBMB")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonLocator)).click();
    }
}

