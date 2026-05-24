package com.example.citas_medicas_api.role.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.role.DTO.RolRequest;
import com.example.citas_medicas_api.role.service.RolService;
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
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles y Permisos", description = "Gestión de roles y asignación de permisos — solo ADMINISTRATIVO")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMINISTRATIVO')")
public class RolController {

    private final RolService rolService;

    @Operation(summary = "Listar todos los roles con sus permisos")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(rolService.listarTodos()));
    }

    @Operation(summary = "Buscar rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.buscarPorId(id)));
    }

    @Operation(summary = "Crear nuevo rol con permisos")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> crear(
            @Valid @RequestBody RolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(rolService.crear(request), "Rol creado exitosamente"));
    }

    @Operation(summary = "Actualizar descripción y permisos del rol")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(
                rolService.actualizar(id, request), "Rol actualizado"));
    }

    @Operation(summary = "Asignar/reemplazar permisos a un rol",
            description = "Enviar el conjunto completo de IDs de permisos que debe tener el rol.")
    @PutMapping("/{id}/permisos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> asignarPermisos(
            @PathVariable Long id,
            @RequestBody Set<Long> permisoIds) {
        return ResponseEntity.ok(ApiResponse.ok(
                rolService.asignarPermisos(id, permisoIds),
                "Permisos asignados al rol"));
    }

    @Operation(summary = "Inactivar rol")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inactivar(@PathVariable Long id) {
        rolService.inactivar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Rol inactivado"));
    }
}
