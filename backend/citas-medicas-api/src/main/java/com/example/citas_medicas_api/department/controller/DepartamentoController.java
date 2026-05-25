package com.example.citas_medicas_api.department.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.department.DTO.DepartamentoRequest;
import com.example.citas_medicas_api.department.model.Departamento;
import com.example.citas_medicas_api.department.service.DepartamentoService;
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
@RequestMapping("/departamentos")
@RequiredArgsConstructor
@Tag(name = "Departamentos", description = "Catálogo maestro de departamentos (DANE)")
@SecurityRequirement(name = "bearerAuth")
public class DepartamentoController {

    private final DepartamentoService service;

    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_READ')")
    public ResponseEntity<ApiResponse<List<Departamento>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.listarActivos()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_READ')")
    public ResponseEntity<ApiResponse<Departamento>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_CREATE')")
    public ResponseEntity<ApiResponse<Departamento>> crear(
            @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.crear(request), "Departamento creado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_UPDATE')")
    public ResponseEntity<ApiResponse<Departamento>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_DELETE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Departamento inactivado"));
    }
}
