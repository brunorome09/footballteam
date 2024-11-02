package com.teammanagement.footballteam.controller;
import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.repo.TeamRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipos")
public class TeamController {

    public static final String PATH_ID = "/{id}";
    public static final String ID = "id";
    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus NO_CONTENT = HttpStatus.NO_CONTENT;
    public static final HttpStatus OK = HttpStatus.OK;
    public static final String MENSAJE = "mensaje";
    public static final String CODIGO = "codigo";
    public static final String NO_ENCONTRADO = "Equipo no encontrado";
    public static final ResponseEntity<Object> RESPONSE_NOT_FOUND = ResponseEntity.status(NOT_FOUND).body(Map.of(MENSAJE, NO_ENCONTRADO, CODIGO, NOT_FOUND.value()));


    @Autowired(required = false)
    private TeamRepo teamRepo;

    @GetMapping()
    public ResponseEntity<List<Team>> getAllTeam() {
        try {
            List<Team> teams = new ArrayList<>();
            teams.addAll(teamRepo.findAll());

            if (teams.isEmpty()){
                return new ResponseEntity<>(NO_CONTENT);
            }

            return new ResponseEntity<>(teams, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(PATH_ID)
    public ResponseEntity<Object> getTeamById(@PathVariable(ID) Long teamId) {
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            return new ResponseEntity<>(teamData, OK);
        }
        return RESPONSE_NOT_FOUND;
    }


    @GetMapping("/buscar")
    public  ResponseEntity<Object> getTeamsbyName(@RequestParam("nombre") String nombre) {
        List<Team> teams = new ArrayList<Team>();
        teams.addAll(teamRepo.findAll());
        teams = teams.stream()
                .filter(team -> team.getNombre() != null && StringUtils.compareIgnoreCase(team.getNombre(), nombre) == 0)
                .collect(Collectors.toList());

        if (teams.isEmpty()) {
            return RESPONSE_NOT_FOUND;
        }

        return new ResponseEntity<>(teams, OK);
    }

    @PostMapping()
    public ResponseEntity<Object> addTeam(@RequestBody Team team) {
        try {
            Team teamSave = teamRepo.save(team);
            return new ResponseEntity<>(teamSave, OK);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(Map.of(MENSAJE, "La solicitud es invalida", CODIGO, NOT_FOUND.value()));
        }
    }

    @PostMapping(PATH_ID)
    public ResponseEntity<Team> updateTeamById(@PathVariable(ID) Long teamId, @RequestBody Team newTeamData) {
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            try {
                Team team = teamData.get();
                team.setNombre(newTeamData.getNombre());
                team.setLiga(newTeamData.getLiga());
                team.setPais(newTeamData.getPais());
                teamRepo.save(team);
                teamRepo.flush();
                return new ResponseEntity<>(team, OK);
            } catch (Exception e) {
                return new ResponseEntity<>(NOT_FOUND);
            }
        }
        return new ResponseEntity<>(NOT_FOUND);
    }

    @DeleteMapping(PATH_ID)
    public ResponseEntity<Object> deleteTeamById(@PathVariable(ID) Long teamId){
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            teamRepo.delete(teamData.get());
            return new ResponseEntity<>(NO_CONTENT);
        }
        return RESPONSE_NOT_FOUND;
    }
}
