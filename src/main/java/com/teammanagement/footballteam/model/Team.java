package com.teammanagement.footballteam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private String nombre;
    private String pais;
    private String liga;

    public Team(String nombre, String liga, String pais) {
        this.nombre = this.nombre;
        this.pais = this.pais;
        this.liga = this.liga;
    }

    public Team(){
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLiga() {
        return liga;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }

    public Long getTeamid() {
        return team_id;
    }
}
