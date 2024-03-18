package org.example.entity;

import lombok.Data;


@Data
public class User {

    private String id;
    private String name;
    private String email;

    public String getPassword() {
        return System.getenv("PASSWORD");
    }
}
