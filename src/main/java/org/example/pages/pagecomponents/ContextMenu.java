package org.example.pages.pagecomponents;

import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;

public class ContextMenu {
    @Getter
    public static final Button deleteElementButton = new Button(By.
            xpath("//span[text()='Удалить']"), "Delete element");
}
