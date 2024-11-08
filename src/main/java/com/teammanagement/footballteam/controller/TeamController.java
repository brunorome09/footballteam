package com.teammanagement.footballteam.controller;

import com.teammanagement.footballteam.exception.ResourceNotFoundException;
import com.teammanagement.footballteam.model.Team;
import com.teammanagement.footballteam.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/equipos")
@Tag(name = "Equipos", description = "API para gestionar equipos de fútbol")
public class TeamController {

    private static final String PATH_ID = "/{id}";
    private static final String ID = "id";
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus NO_CONTENT = HttpStatus.NO_CONTENT;
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus OK = HttpStatus.OK;
    private static final HttpStatus CREATED = HttpStatus.CREATED;
    private static final String MENSAJE = "mensaje";
    private static final String CODIGO = "codigo";
    private static final String NO_ENCONTRADO = "Equipo no encontrado";
    private static final ResponseEntity<Object> RESPONSE_NOT_FOUND = ResponseEntity.status(NOT_FOUND).body(Map.of(MENSAJE, NO_ENCONTRADO, CODIGO, NOT_FOUND.value()));

    @Autowired(required = false)
    private TeamService teamService;

    @GetMapping
    @Operation(summary = "Obtener todos los equipos", description = "Retorna una lista de todos los equipos de fútbol cargados en la base de datos")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "204", description = "No hay equipos disponibles")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(teams, OK);
    }

    @GetMapping(PATH_ID)
    @Operation(summary = "Obtener un equipo por ID", description = "Retorna un equipo de fútbol basado en su ID")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    public ResponseEntity<Object> getTeamById(@PathVariable(ID) Long teamId) {
        Optional<Team> teamData = teamService.getTeamById(teamId);
        return teamData.<ResponseEntity<Object>>map(team -> new ResponseEntity<>(team, OK))
                .orElse(RESPONSE_NOT_FOUND);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar equipos por nombre", description = "Retorna una lista de equipos que coinciden con el nombre proporcionado")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron equipos")
    public ResponseEntity<Object> getTeamsByName(@RequestParam("nombre") String nombre) {
        try {
            List<Team> teams = teamService.getTeamsByName(nombre);
            return new ResponseEntity<>(teams, OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(Map.of(MENSAJE, e.getMessage(), CODIGO, NOT_FOUND.value()) , NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Agregar un nuevo equipo", description = "Crea un nuevo equipo de fútbol")
    @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente", content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad de datos")
    public ResponseEntity<Object> addTeam(@Valid @RequestBody Team team, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(Map.of("error", errors.toString()), BAD_REQUEST);
        }
        try {
            Team savedTeam = teamService.saveTeam(team);
            return new ResponseEntity<>(savedTeam, CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(MENSAJE, "El equipo ya existe o viola una restricción de integridad", CODIGO, HttpStatus.CONFLICT.value()));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(Map.of(MENSAJE, "La solicitud es invalida", CODIGO, NOT_FOUND.value()));
        }
    }

    @PutMapping(PATH_ID)
    @Operation(summary = "Actualizar un equipo", description = "Actualiza los datos de un equipo existente")
    @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente", content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad de datos")
    public ResponseEntity<Object> updateTeamById(@PathVariable(ID) Long teamId, @Valid @RequestBody Team newTeamData, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(Map.of("error", errors.toString()), BAD_REQUEST);
        }
        try {
            Team updatedTeam = teamService.updateTeam(teamId, newTeamData);
            if (updatedTeam != null) {
                return new ResponseEntity<>(updatedTeam, OK);
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(MENSAJE, "Viola una restricción de integridad", CODIGO, HttpStatus.CONFLICT.value()));
        }
        return RESPONSE_NOT_FOUND;
    }

    @DeleteMapping(PATH_ID)
    @Operation(summary = "Eliminar un equipo", description = "Elimina un equipo existente por su ID")
    @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    public ResponseEntity<Object> deleteTeamById(@PathVariable(ID) Long teamId) {
        boolean isDeleted = teamService.deleteTeam(teamId);
        if (isDeleted) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return RESPONSE_NOT_FOUND;
    }
}