package com.teammanagement.footballteam.service;

import com.teammanagement.footballteam.exception.ResourceNotFoundException;
import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.repo.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    public static final String ERROR_TEAM = "No se encontraron equipos con el nombre: ";
    @Autowired(required = false)
    private TeamRepo teamRepo;

    public List<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    public Optional<Team> getTeamById(Long teamId) {
        return teamRepo.findById(teamId);
    }

    public List<Team> getTeamsByName(String nombre) {
        List<Team> teams = teamRepo.findByNombreIgnoreCase(nombre);
        if (teams.isEmpty()) {
            throw new ResourceNotFoundException(ERROR_TEAM + nombre);
        }
        return teams;
    }

    public Team saveTeam(Team team) throws DataIntegrityViolationException {
        try {
            return teamRepo.save(team);
        } catch (DataIntegrityViolationException e) {
         throw e;
        }
    }

    public Team updateTeam(Long teamId, Team newTeamData) throws DataIntegrityViolationException {
        Optional<Team> teamOptional = teamRepo.findById(teamId);
        if (teamOptional.isPresent()) {
            try {
                Team team = teamOptional.get();
                team.setNombre(newTeamData.getNombre());
                team.setLiga(newTeamData.getLiga());
                team.setPais(newTeamData.getPais());
                teamRepo.save(team);
                return team;
            } catch (DataIntegrityViolationException e) {
                throw e;
            }
        }
        return null;
    }

    public boolean deleteTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        if (team.isPresent()) {
            teamRepo.delete(team.get());
            return true;
        }
        return false;
    }
}