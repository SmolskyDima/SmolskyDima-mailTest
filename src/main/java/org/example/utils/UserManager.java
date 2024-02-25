package org.example.utils;

import org.example.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserManager {

    private volatile static UserManager userManager;
    private Map<String, User> users;
    private static final String CONFIG_FILE = "config.properties";

    private UserManager() {
        users = new HashMap<>();
        loadUsersFromConfigFile();
    }

    public static UserManager getUserManager() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    private void loadUsersFromConfigFile() {
        Properties userData = new Properties();
        try {
            userData.load(UserManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            String username = userData.getProperty("username");
            String password = userData.getProperty("password");
            String email = userData.getProperty("email");
            users.put("username", new User(username, password, email));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserByName(String username) {
        return users.get(username);
    }
}
