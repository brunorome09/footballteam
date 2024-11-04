package com.teammanagement.footballteam.controller;

import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/equipos")
public class TeamController {

    private static final String PATH_ID = "/{id}";
    private static final String ID = "id";
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus NO_CONTENT = HttpStatus.NO_CONTENT;
    private static final HttpStatus OK = HttpStatus.OK;
    private static final String MENSAJE = "mensaje";
    private static final String CODIGO = "codigo";
    private static final String NO_ENCONTRADO = "Equipo no encontrado";
    private static final ResponseEntity<Object> RESPONSE_NOT_FOUND = ResponseEntity.status(NOT_FOUND).body(Map.of(MENSAJE, NO_ENCONTRADO, CODIGO, NOT_FOUND.value()));

    @Autowired(required = false)
    private TeamService teamService;


    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(teams, OK);
    }

    @GetMapping(PATH_ID)
    public ResponseEntity<Object> getTeamById(@PathVariable(ID) Long teamId) {
        Optional<Team> teamData = teamService.getTeamById(teamId);
        return teamData.<ResponseEntity<Object>>map(team -> new ResponseEntity<>(team, OK))
                .orElse(RESPONSE_NOT_FOUND);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Object> getTeamsByName(@RequestParam("nombre") String nombre) {
        List<Team> teams = teamService.getTeamsByName(nombre);
        if (teams.isEmpty()) {
            return RESPONSE_NOT_FOUND;
        }
        return new ResponseEntity<>(teams, OK);
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestBody Team team) {
        try {
            Team savedTeam = teamService.saveTeam(team);
            return new ResponseEntity<>(savedTeam, OK);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(Map.of(MENSAJE, "La solicitud es invalida", CODIGO, NOT_FOUND.value()));
        }
    }

    @PostMapping(PATH_ID)
    public ResponseEntity<Object> updateTeamById(@PathVariable(ID) Long teamId, @RequestBody Team newTeamData) {
        Team updatedTeam = teamService.updateTeam(teamId, newTeamData);
        if (updatedTeam != null) {
            return new ResponseEntity<>(updatedTeam, OK);
        }
        return RESPONSE_NOT_FOUND;
    }

    @DeleteMapping(PATH_ID)
    public ResponseEntity<Object> deleteTeamById(@PathVariable(ID) Long teamId) {
        boolean isDeleted = teamService.deleteTeam(teamId);
        if (isDeleted) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return RESPONSE_NOT_FOUND;
    }
}