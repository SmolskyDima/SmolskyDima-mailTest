package org.example.pages.pagecomponents;

import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;

public class ContextMenu {
    @Getter
    public static final Button deleteElementButton = new Button(By.
            xpath("//div[5]/div/ul/li[16]/a/span"), "Delete element");
}
