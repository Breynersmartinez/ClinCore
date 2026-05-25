package com.example.citas_medicas_api.headquarters.controller;

import com.example.citas_medicas_api.headquarters.DTO.SedeRequest;
import com.example.citas_medicas_api.headquarters.DTO.SedeResponse;
import com.example.citas_medicas_api.headquarters.service.SedeService;
import com.example.citas_medicas_api.config.DTO.ApiResponse;
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
@RequestMapping("/sedes")
@RequiredArgsConstructor
@Tag(name = "Sedes", description = "Puntos de atención de la institución médica")
@SecurityRequirement(name = "bearerAuth")
public class SedeController {

    private final SedeService service;

    @GetMapping
    @PreAuthorize("hasAuthority('SEDES_READ')")
    public ResponseEntity<ApiResponse<List<SedeResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(service.listarActivas()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SEDES_READ')")
    public ResponseEntity<ApiResponse<SedeResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SEDES_CREATE')")
    public ResponseEntity<ApiResponse<SedeResponse>> crear(@Valid @RequestBody SedeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.crear(request), "Sede creada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SEDES_UPDATE')")
    public ResponseEntity<ApiResponse<SedeResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody SedeRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SEDES_DELETE')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Sede inactivada"));
    }
}
