package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private volatile static UserManager userManager;
    private static Map<String, User> users;
    private static final String USERS_FILE = "src/main/resources/users.json";

    static {
        initializeUsers();
    }

    public static void initializeUsers() {
        users = loadUsersFromJsonFile();
    }

    private static Map<String, User> loadUsersFromJsonFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                User[] userList = mapper.readValue(file, User[].class);
                Map<String, User> userMap = new HashMap<>();
                for (User user : userList) {
                    userMap.put(user.getName(), user);
                }
                return userMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static User getUserByName(String username) {
        return users.get(username);
    }
}
