package com.teammanagement.footballteam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class User {
    @Id
    private UUID user_id;

    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.user_id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getTeamid() {
        return user_id;
    }
}
