package com.example.citas_medicas_api.medicalAppointment.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.medicalAppointment.DTO.CitaMedicaRequest;
import com.example.citas_medicas_api.medicalAppointment.DTO.CitaMedicaResponse;
import com.example.citas_medicas_api.medicalAppointment.service.CitaMedicaService;
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
 * CRUD de Citas Médicas.
 *
 * Acceso por rol:
 *  GET         → todos los roles autenticados
 *  POST        → ADMINISTRATIVO, AUXILIAR_MEDICO
 *  PUT         → MEDICO (estado), ADMINISTRATIVO, AUXILIAR_MEDICO
 *  DELETE      → solo ADMINISTRATIVO
 */
@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
@Tag(name = "Citas Médicas", description = "Agendamiento y gestión de citas médicas")
@SecurityRequirement(name = "bearerAuth")
public class CitaMedicaController {

    private final CitaMedicaService citaService;

    @Operation(summary = "Listar todas las citas")
    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<List<CitaMedicaResponse>>> listarTodas() {
        return ResponseEntity.ok(ApiResponse.ok(citaService.listarTodas()));
    }

    @Operation(summary = "Buscar cita por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<CitaMedicaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.buscarPorId(id)));
    }

    @Operation(summary = "Listar citas de un paciente")
    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyRole('MEDICO','PACIENTE','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<List<CitaMedicaResponse>>> porPaciente(
            @PathVariable Long idPaciente) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.listarPorPaciente(idPaciente)));
    }

    @Operation(summary = "Listar citas de un médico")
    @GetMapping("/medico/{idMedico}")
    @PreAuthorize("hasAnyRole('MEDICO','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<List<CitaMedicaResponse>>> porMedico(
            @PathVariable Long idMedico) {
        return ResponseEntity.ok(ApiResponse.ok(citaService.listarPorMedico(idMedico)));
    }

    @Operation(summary = "Agendar nueva cita",
            description = "⚠ ADMINISTRATIVO y AUXILIAR_MEDICO. Valida solapamiento de horarios.")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<CitaMedicaResponse>> crear(
            @Valid @RequestBody CitaMedicaRequest request) {
        CitaMedicaResponse cita = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(cita, "Cita agendada: " + cita.getNumeroCita()));
    }

    @Operation(summary = "Modificar cita",
            description = "⚠ ADMINISTRATIVO y AUXILIAR_MEDICO. Solo citas PROGRAMADA/CONFIRMADA.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<CitaMedicaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaMedicaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                citaService.actualizar(id, request), "Cita actualizada"));
    }

    @Operation(summary = "Actualizar estado de una cita",
            description = "MEDICO puede confirmar/atender. ADMINISTRATIVO puede cancelar. " +
                    "Estados: PROGRAMADA → CONFIRMADA → ATENDIDA / CANCELADA / NO_ASISTIO")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('MEDICO','ADMINISTRATIVO','AUXILIAR_MEDICO')")
    public ResponseEntity<ApiResponse<CitaMedicaResponse>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(ApiResponse.ok(
                citaService.actualizarEstado(id, estado, observaciones),
                "Estado actualizado a: " + estado));
    }

    @Operation(summary = "Cancelar cita",
            description = "⚠ Solo ADMINISTRATIVO. Cancela la cita con un motivo.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATIVO')")
    public ResponseEntity<ApiResponse<Void>> cancelar(
            @PathVariable Long id,
            @RequestParam String motivo) {
        citaService.cancelar(id, motivo);
        return ResponseEntity.ok(ApiResponse.ok(null, "Cita cancelada"));
    }
}