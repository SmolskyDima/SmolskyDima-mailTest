package org.example.pages.pagecomponents;

import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.example.driver.Waiter.getWaiter;

@Getter
public class SaveDocumentPopup {

    private final Button myDocumentButton = new Button(By.xpath("//div[text()='Мои документы']"), "My document");
    private final Button saveButton = new Button(By.xpath("//div[@id='dialBtn_OK']"), "Save");

    public void clickMyDocuments() {
        getMyDocumentButton().click();
    }

    public void clickSaveButton() {
        getWaiter().withMessage("Waiting for the save button to become active").until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButton.getLocator(), "class", "GCSDBRWBMB")));
        saveButton.click();
    }
}

