package com.example.citas_medicas_api.permission.controller;

import com.example.citas_medicas_api.config.DTO.ApiResponse;
import com.example.citas_medicas_api.permission.DTO.PermisoResponseDTO;
import com.example.citas_medicas_api.permission.service.PermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permisos")
@RequiredArgsConstructor
@Tag(name = "Permisos", description = "Consulta de permisos de aplicación")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMINISTRATIVO')")
public class PermisoController {

    private final PermisoService permisoService;

    @Operation(summary = "Listar permisos",
            description = "Permite listar todos los permisos o filtrarlos por módulo usando ?modulo=CONSULTORIOS")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermisoResponseDTO>>> listar(
            @RequestParam(required = false) String modulo) {
        return ResponseEntity.ok(ApiResponse.ok(permisoService.listar(modulo)));
    }
}
