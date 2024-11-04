package com.teammanagement.footballteam.service;

import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.repo.TeamRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired(required = false)
    private TeamRepo teamRepo;

    public List<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    public Optional<Team> getTeamById(Long teamId) {
        return teamRepo.findById(teamId);
    }

    public List<Team> getTeamsByName(String nombre) {
        return teamRepo.findAll().stream()
                .filter(team -> team.getNombre() != null && StringUtils.compareIgnoreCase(team.getNombre(), nombre) == 0)
                .collect(Collectors.toList());
    }

    public Team saveTeam(Team team) {
        return teamRepo.save(team);
    }

    public Team updateTeam(Long teamId, Team newTeamData) {
        Optional<Team> teamOptional = teamRepo.findById(teamId);
        if (teamOptional.isPresent()) {
                Team team = teamOptional.get();
                team.setNombre(newTeamData.getNombre());
                team.setLiga(newTeamData.getLiga());
                team.setPais(newTeamData.getPais());
                teamRepo.save(team);
                return team;
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