package org.example.pages.pagecomponents;

import io.qameta.allure.Step;
import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.example.driver.Waiter.getWaiter;

@Getter
public class SaveDocumentPopup {

    private final Button myDocumentButton = new Button(By.xpath("//div[5]/div[2]/div/div[2]/div/div/div/div/div[1]/div[2]"), "My document");

    private final Button saveButton = new Button(By.xpath("//div[@id='dialBtn_OK']"), "Save");

    @Step("Click on the button 'My documents' in my documents pop-up")
    public void clickMyDocuments() {
        getMyDocumentButton().click();
    }

    @Step("Click on yhe button 'save document' in my documents pop-up")
    public void clickSaveButton() {
        getWaiter().withMessage("Waiting for the save button to become active").until(ExpectedConditions.not(ExpectedConditions
                .attributeContains(saveButton.getLocator(), "class", "GCSDBRWBMB")));
        saveButton.click();
    }
}

