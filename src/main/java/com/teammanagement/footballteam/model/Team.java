package com.teammanagement.footballteam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class Team {
    @Id
    private UUID team_id;

    private String name;
    private String country;
    private String league;

    public Team(Integer team_id, String name, String league, String country) {
        this.name = name;
        this.country = country;
        this.league = league;
        this.team_id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public UUID getTeamid() {
        return team_id;
    }
}
