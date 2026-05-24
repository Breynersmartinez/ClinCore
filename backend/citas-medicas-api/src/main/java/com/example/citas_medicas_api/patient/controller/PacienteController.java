package com.example.citas_medicas_api.patient.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.patient.DTO.PacienteRequest;
import com.example.citas_medicas_api.patient.DTO.PacienteResponse;
import com.example.citas_medicas_api.patient.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gestión de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacienteController {

    private final PacienteService pacienteService;

    @Operation(summary = "Listar todos los pacientes")
    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<List<PacienteResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.listarTodos()));
    }

    @Operation(summary = "Listar pacientes activos")
    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<List<PacienteResponse>>> listarActivos() {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.listarActivos()));
    }

    @Operation(summary = "Buscar paciente por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<PacienteResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.buscarPorId(id)));
    }

    @Operation(summary = "Buscar paciente por persona")
    @GetMapping("/persona/{idPersona}")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<PacienteResponse>> buscarPorPersona(
            @PathVariable Long idPersona) {
        return ResponseEntity.ok(ApiResponse.ok(pacienteService.buscarPorPersona(idPersona)));
    }

    @Operation(summary = "Crear paciente")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<PacienteResponse>> crear(
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse created = pacienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Paciente creado exitosamente"));
    }

    @Operation(summary = "Actualizar paciente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATIVO')")
    public ResponseEntity<ApiResponse<PacienteResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                pacienteService.actualizar(id, request),
                "Paciente actualizado exitosamente"));
    }

    @Operation(summary = "Inactivar paciente")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATIVO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Paciente inactivado exitosamente"));
    }
}
