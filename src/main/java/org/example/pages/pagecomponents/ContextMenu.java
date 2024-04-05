package org.example.pages.pagecomponents;

import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ContextMenu {

    private static final Button deleteElementButton = new Button(By.xpath("//span[text()='Удалить']"), "Delete element");

    public static WebElement getDeleteElementButton() {
        return deleteElementButton.getElement();
    }
}
