package com.teammanagement.footballteam.service;


import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.repo.TeamRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTest {
    @Mock
    private TeamRepo teamRepo;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private Team newTeam;
    private static final Long TEAMID = 100L;
    public static final String INDEPENDIENTE = "Independiente";
    public static final String REAL_MADRID = "Real Madrid";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        team = new Team();
        team.setNombre(INDEPENDIENTE);
        team.setLiga("LPF");
        team.setPais("Argentina");

        newTeam = new Team();
        newTeam.setNombre(REAL_MADRID);
        newTeam.setLiga("La Liga");
        newTeam.setPais("España");
    }

    @Test
    public void testGetAllTeams(){
        when(teamService.getAllTeams()).thenReturn(Arrays.asList(team, newTeam));
        List<Team> result = teamService.getAllTeams();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(INDEPENDIENTE, result.get(0).getNombre());
        assertEquals(REAL_MADRID, result.get(1).getNombre());
    }

    @Test
    public void testSaveTeam(){
        when(teamService.saveTeam(any(Team.class))).thenReturn(team);
        Team result = teamService.saveTeam(team);
        assertNotNull(result);
        assertEquals(INDEPENDIENTE, result.getNombre());
        verify(teamRepo).save(team);
    }

    @Test
    public void testDeleteTeamExist(){
        when(teamRepo.findById(TEAMID)).thenReturn(Optional.of(team));
        boolean result = teamService.deleteTeam(TEAMID);
        assertTrue(result);
        verify(teamRepo).delete(team);
    }

    @Test
    public void testDeleteTeamNotExist(){
        when(teamRepo.findById(TEAMID)).thenReturn(Optional.empty());
        boolean result = teamService.deleteTeam(TEAMID);
        assertFalse(result);
        verify(teamRepo, never()).delete(team);
    }

    @Test
    public void testUpdateTeamExist(){

        when(teamRepo.findById(TEAMID)).thenReturn(Optional.of(team));

        Team updatedTeam = teamService.updateTeam(TEAMID, newTeam);

        verify(teamRepo).save(any(Team.class));

        assertNotNull(updatedTeam);
        assertEquals(REAL_MADRID, updatedTeam.getNombre());
        assertEquals("La Liga", updatedTeam.getLiga());
        assertEquals("España", updatedTeam.getPais());
    }

    @Test
    public void testUpdateTeamNotExists() {
        when(teamRepo.findById(TEAMID)).thenReturn(Optional.empty());

        Team updatedTeam = teamService.updateTeam(TEAMID, newTeam);

        verify(teamRepo, never()).save(any(Team.class));

        assertNull(updatedTeam);
    }

    @Test
    public void testGetTeamById() {
        when(teamRepo.findById(TEAMID)).thenReturn(Optional.of(team));
        Optional<Team> result = teamService.getTeamById(TEAMID);
        assertTrue(result.isPresent());
        assertEquals(INDEPENDIENTE, result.get().getNombre());
    }

    @Test
    public void testGetTeamByIdNotExist() {
        when(teamRepo.findById(TEAMID)).thenReturn(Optional.empty());
        Optional<Team> result = teamService.getTeamById(TEAMID);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetTeamsByName() {
        when(teamRepo.findAll()).thenReturn(Arrays.asList(team, newTeam));
        List<Team> result = teamService.getTeamsByName(INDEPENDIENTE);
        assertEquals(1, result.size());
        assertEquals(INDEPENDIENTE, result.get(0).getNombre());
    }

    @Test
    public void testGetTeamsByNameNoMatch() {
        when(teamRepo.findAll()).thenReturn(Arrays.asList(team, newTeam));
        List<Team> result = teamService.getTeamsByName("Barcelona");
        assertTrue(result.isEmpty());
    }
}