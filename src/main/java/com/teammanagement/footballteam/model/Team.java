package com.teammanagement.footballteam.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


@Entity
@Schema(description = "Representa un equipo de fútbol")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long team_id;

    @NotBlank(message = "El nombre del equipo no puede estar vacio")
    @Schema(example = "Real Madrid")
    private String nombre;

    @NotBlank(message = "El pais del equipo no puede estar vacio")
    @Schema(example = "España")
    private String pais;

    @NotBlank(message = "La liga del equipo no puede estar vacia")
    @Schema(example = "La Liga")
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
