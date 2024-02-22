package org.example.pages.pagecomponents;

import org.openqa.selenium.By;


public class NavigationBar {

    private final By lettersButton = By.id("nav-mail");

    private final By docsButton = By.id("nav-docs");

    public By getLettersButton() {
        return lettersButton;
    }

    public By getDocsButton() {
        return docsButton;
    }
}
