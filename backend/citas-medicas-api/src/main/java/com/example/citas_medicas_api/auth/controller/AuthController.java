package com.example.citas_medicas_api.auth.controller;

import com.example.citas_medicas_api.auth.DTO.AuthResponse;
import com.example.citas_medicas_api.auth.DTO.LoginRequest;
import com.example.citas_medicas_api.auth.service.AuthService;
import com.example.citas_medicas_api.config.DTO.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login, logout y refresh de tokens JWT")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve access token + refresh token. " +
                    "Incluye los roles y permisos del usuario autenticado.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Login exitoso"));
    }

    @Operation(summary = "Renovar token de acceso",
            description = "Genera un nuevo access token usando el refresh token válido.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestParam String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.ok(response, "Token renovado exitosamente"));
    }

    @Operation(summary = "Cerrar sesión",
            description = "Invalida la sesión del usuario (el cliente debe eliminar los tokens).")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // Con JWT stateless, el logout es responsabilidad del cliente (eliminar tokens)
        return ResponseEntity.ok(ApiResponse.ok(null, "Sesión cerrada exitosamente"));
    }
}
