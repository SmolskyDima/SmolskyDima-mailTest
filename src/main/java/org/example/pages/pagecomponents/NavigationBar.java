package org.example.pages.pagecomponents;

import org.example.elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class NavigationBar {

    private static final Button lettersButton = new Button(By.id("nav-mail"), "Letters");

    private static final Button documentsButton = new Button(By.id("nav-docs"), "Documents");

    public static WebElement getLettersButton() {
        return lettersButton.getElement();
    }

    public static WebElement getDocumentsButton() {
        return documentsButton.getElement();
    }

}
