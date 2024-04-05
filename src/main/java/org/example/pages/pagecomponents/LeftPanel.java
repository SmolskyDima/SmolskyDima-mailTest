package org.example.pages.pagecomponents;

import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LeftPanel {

    private static final Button trashButton = new Button(By.xpath("//div[@id='doc_tree_trash']"), "Trash button");

    public static WebElement getTrashButton() {
        return trashButton.getElement();
    }
}