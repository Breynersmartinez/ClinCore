package com.example.citas_medicas_api.user.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class UsuarioRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Email
    private String email;

    private Long idPersona;

    private Set<Long> roleIds;
}