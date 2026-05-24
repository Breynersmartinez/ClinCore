package com.example.citas_medicas_api.user.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.user.DTO.UsuarioRequest;
import com.example.citas_medicas_api.user.service.UsuarioService;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema — solo ADMINISTRATIVO")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMINISTRATIVO')")  // Aplica a todo el controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.listarTodos()));
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.buscarPorId(id)));
    }

    @Operation(summary = "Crear usuario con roles")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> crear(
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(usuarioService.crear(request), "Usuario creado"));
    }

    @Operation(summary = "Asignar/reemplazar roles a un usuario")
    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<Map<String, Object>>> asignarRoles(
            @PathVariable Long id,
            @RequestBody Set<Long> roleIds) {
        return ResponseEntity.ok(ApiResponse.ok(
                usuarioService.actualizarRoles(id, roleIds),
                "Roles actualizados"));
    }

    @Operation(summary = "Cambiar contraseña de usuario")
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @PathVariable Long id,
            @RequestParam String nuevaPassword) {
        usuarioService.cambiarPassword(id, nuevaPassword);
        return ResponseEntity.ok(ApiResponse.ok(null, "Contraseña actualizada"));
    }

    @Operation(summary = "Inactivar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> inactivar(@PathVariable Long id) {
        usuarioService.inactivar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario inactivado"));
    }

    @Operation(summary = "Activar usuario")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<Void>> activar(@PathVariable Long id) {
        usuarioService.activar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario activado"));
    }
}