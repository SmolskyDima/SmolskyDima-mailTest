package org.example.pages.pagecomponents;

import lombok.Getter;
import org.example.elements.Button;
import org.openqa.selenium.By;

public class NavigationBar {
    @Getter
    public static final Button documentsButton = new Button(By.id("nav-docs"), "Documents");
}
