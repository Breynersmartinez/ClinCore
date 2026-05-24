package com.example.citas_medicas_api.auth.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
