package com.example.citas_medicas_api.doctor.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.doctor.DTO.MedicoRequest;
import com.example.citas_medicas_api.doctor.DTO.MedicoResponse;
import com.example.citas_medicas_api.doctor.service.MedicoService;
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
@RequestMapping("/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Gestión de médicos")
@SecurityRequirement(name = "bearerAuth")
public class MedicoController {

    private final MedicoService medicoService;

    @Operation(summary = "Listar todos los médicos")
    @GetMapping
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.listarTodos()));
    }

    @Operation(summary = "Listar médicos activos")
    @GetMapping("/activos")
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarActivos() {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.listarActivos()));
    }

    @Operation(summary = "Buscar médico por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<MedicoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.buscarPorId(id)));
    }

    @Operation(summary = "Buscar médico por persona")
    @GetMapping("/persona/{idPersona}")
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<MedicoResponse>> buscarPorPersona(
            @PathVariable Long idPersona) {
        return ResponseEntity.ok(ApiResponse.ok(medicoService.buscarPorPersona(idPersona)));
    }

    @Operation(summary = "Listar médicos por especialidad")
    @GetMapping("/especialidad/{idEspecialidad}")
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarPorEspecialidad(
            @PathVariable Long idEspecialidad) {
        return ResponseEntity.ok(ApiResponse.ok(
                medicoService.listarPorEspecialidad(idEspecialidad)));
    }

    @Operation(summary = "Listar médicos activos por especialidad")
    @GetMapping("/especialidad/{idEspecialidad}/activos")
    @PreAuthorize("hasAuthority('MEDICOS_READ')")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarActivosPorEspecialidad(
            @PathVariable Long idEspecialidad) {
        return ResponseEntity.ok(ApiResponse.ok(
                medicoService.listarActivosPorEspecialidad(idEspecialidad)));
    }

    @Operation(summary = "Crear médico")
    @PostMapping
    @PreAuthorize("hasAuthority('MEDICOS_CREATE')")
    public ResponseEntity<ApiResponse<MedicoResponse>> crear(
            @Valid @RequestBody MedicoRequest request) {
        MedicoResponse created = medicoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Médico creado exitosamente"));
    }

    @Operation(summary = "Actualizar médico")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICOS_UPDATE')")
    public ResponseEntity<ApiResponse<MedicoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                medicoService.actualizar(id, request),
                "Médico actualizado exitosamente"));
    }

    @Operation(summary = "Inactivar médico")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICOS_DELETE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Médico inactivado exitosamente"));
    }
}
