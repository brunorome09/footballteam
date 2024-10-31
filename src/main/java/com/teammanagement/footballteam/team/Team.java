package com.teammanagement.footballteam.team;

public class Team {
    private Integer team_id;
    private String name;
    private String country;
    private String league;

    public Team(Integer team_id, String name, String league, String country) {
        this.name = name;
        this.country = country;
        this.league = league;
        this.team_id = team_id;
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

    public Integer getTeamid() {
        return team_id;
    }

    public void setTeamid(Integer team_id) {
        this.team_id = team_id;
    }
}
