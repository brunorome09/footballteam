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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipos")
public class TeamController {

    public static final String PATH_ID = "/{id}";
    public static final String ID = "id";


    @Autowired(required = false)
    private TeamRepo teamRepo;

    @GetMapping()
    public ResponseEntity<List<Team>> getAllTeam() {
        try {
            List<Team> teams = new ArrayList<Team>();
            teams.addAll(teamRepo.findAll());

            if (teams.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(PATH_ID)
    public ResponseEntity<Team> getTeamById(@PathVariable(ID) UUID teamId) {
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            return new ResponseEntity<>(teamData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buscar")
    public  ResponseEntity<List<Team>> getTeamsbyName(@RequestParam("nombre") String name) {
        List<Team> teams = new ArrayList<Team>();
        teams.addAll(teamRepo.findAll());
        teams = teams.stream()
                .filter(team -> team.getName() != null && StringUtils.compareIgnoreCase(team.getName(), name) == 0)
                .collect(Collectors.toList());

        if (teams.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        try {
            Team teamSave = teamRepo.save(team);
            return new ResponseEntity<>(teamSave, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(PATH_ID)
    public ResponseEntity<Team> updateTeamById(@PathVariable(ID) UUID teamId, @RequestBody Team newTeamData) {
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            try {
                Team team = teamData.get();
                team.setName(newTeamData.getName());
                team.setLeague(newTeamData.getLeague());
                team.setCountry(newTeamData.getCountry());
                teamRepo.save(team);
                teamRepo.flush();
                return new ResponseEntity<>(team, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(PATH_ID)
    public ResponseEntity<HttpStatus> deleteTeamById(@PathVariable(ID) UUID teamId){
        Optional<Team> teamData = teamRepo.findById(teamId);

        if (teamData.isPresent()) {
            teamRepo.delete(teamData.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
