package org.example.pages.pagecomponents;

import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;

public class LeftPanel {
    @Getter
    public static final Button trashButton = new Button(By.
            xpath("//div[@id='doc_tree_trash']"), "Trash button");

}