package com.example.citas_medicas_api.office.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.office.DTO.ConsultorioRequest;
import com.example.citas_medicas_api.office.DTO.ConsultorioResponse;
import com.example.citas_medicas_api.office.service.ConsultorioService;
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

/**
 * CRUD de Consultorios.
 *
 * Control de acceso (doble capa):
 *  - Capa HTTP:  SecurityConfig exige autenticación JWT
 *  - Capa Método: @PreAuthorize valida el permiso específico
 *
 * Matriz de acceso:
 *  GET    → CONSULTORIOS_READ
 *  POST   → CONSULTORIOS_CREATE
 *  PUT    → CONSULTORIOS_UPDATE
 *  DELETE → CONSULTORIOS_DELETE
 */
@RestController
@RequestMapping("/consultorios")
@RequiredArgsConstructor
@Tag(name = "Consultorios", description = "Gestión de consultorios médicos por sede")
@SecurityRequirement(name = "bearerAuth")
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    // ─────────────────────────── LECTURA ───────────────────────────────

    @Operation(summary = "Listar todos los consultorios",
            description = "Acceso: CONSULTORIOS_READ")
    @GetMapping
    @PreAuthorize("hasAuthority('CONSULTORIOS_READ')")
    public ResponseEntity<ApiResponse<List<ConsultorioResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(consultorioService.listarTodos()));
    }

    @Operation(summary = "Buscar consultorio por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CONSULTORIOS_READ')")
    public ResponseEntity<ApiResponse<ConsultorioResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(consultorioService.buscarPorId(id)));
    }

    @Operation(summary = "Listar consultorios por sede")
    @GetMapping("/sede/{idSede}")
    @PreAuthorize("hasAuthority('CONSULTORIOS_READ')")
    public ResponseEntity<ApiResponse<List<ConsultorioResponse>>> porSede(
            @PathVariable Long idSede) {
        return ResponseEntity.ok(ApiResponse.ok(consultorioService.listarPorSede(idSede)));
    }

    @Operation(summary = "Listar consultorios ACTIVOS por sede")
    @GetMapping("/sede/{idSede}/activos")
    @PreAuthorize("hasAuthority('CONSULTORIOS_READ')")
    public ResponseEntity<ApiResponse<List<ConsultorioResponse>>> activosPorSede(
            @PathVariable Long idSede) {
        return ResponseEntity.ok(ApiResponse.ok(consultorioService.listarActivosPorSede(idSede)));
    }

    // ─────────────────────────── ESCRITURA ─────────────────────────────

    @Operation(summary = "Crear consultorio",
            description = "Requiere CONSULTORIOS_CREATE.")
    @PostMapping
    @PreAuthorize("hasAuthority('CONSULTORIOS_CREATE')")
    public ResponseEntity<ApiResponse<ConsultorioResponse>> crear(
            @Valid @RequestBody ConsultorioRequest request) {
        ConsultorioResponse created = consultorioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, "Consultorio creado exitosamente"));
    }

    @Operation(summary = "Actualizar consultorio",
            description = "Requiere CONSULTORIOS_UPDATE.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CONSULTORIOS_UPDATE')")
    public ResponseEntity<ApiResponse<ConsultorioResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConsultorioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                consultorioService.actualizar(id, request),
                "Consultorio actualizado exitosamente"));
    }

    @Operation(summary = "Inactivar consultorio",
            description = "Requiere CONSULTORIOS_DELETE.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CONSULTORIOS_DELETE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        consultorioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Consultorio inactivado exitosamente"));
    }
}
