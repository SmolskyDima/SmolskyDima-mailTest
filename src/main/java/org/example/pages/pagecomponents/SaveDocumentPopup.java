package org.example.pages.pagecomponents;

import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.example.driver.Waiter.getWaiter;

public class SaveDocumentPopup {

    private final Button myDocumentButton = new Button(By.xpath("//div[text()='Мои документы']"), "My document");
    private final Button saveButton = new Button(By.xpath("//div[@id='dialBtn_OK']"), "Save");

    public WebElement getMyDocumentButton() {
        return getWaiter().until(ExpectedConditions.visibilityOfElementLocated(myDocumentButton.getLocator()));
    }

    public WebElement getSaveButton() {
        return getWaiter().until(ExpectedConditions.visibilityOfElementLocated(saveButton.getLocator()));
    }

    public void clickMyDocuments() {
        getMyDocumentButton().click();
    }

    public void clickSaveButton() {
        getWaiter().withMessage("Waiting for the save button to become active").until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButton.getLocator(), "class", "GCSDBRWBMB")));
        getSaveButton().click();
    }
}

