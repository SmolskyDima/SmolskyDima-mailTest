package org.example;

import java.io.IOException;
import java.util.Properties;

public class config {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(config.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Проверьте наличие config.properties", e);
        }
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }
}


