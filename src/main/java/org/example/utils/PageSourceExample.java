package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;

public class PageSourceExample {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.example.com");

        // Получаем исходный код страницы
        String pageSource = driver.getPageSource();

        // Сохраняем исходный код страницы в файл
        try {
            FileWriter fileWriter = new FileWriter("page_source.html");
            fileWriter.write(pageSource);
            fileWriter.close();
            System.out.println("Исходный код страницы сохранен в файл page_source.html");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении исходного кода страницы: " + e.getMessage());
        }

        driver.quit();
    }
}

