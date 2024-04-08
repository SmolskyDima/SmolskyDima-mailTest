package org.example.pages.pagecomponents;

import org.example.elements.Button;
import org.openqa.selenium.By;

public class ContextMenu {
    public static final Button deleteElementButton = new Button(By.
            xpath("//span[text()='Удалить']"), "Delete element");
}
