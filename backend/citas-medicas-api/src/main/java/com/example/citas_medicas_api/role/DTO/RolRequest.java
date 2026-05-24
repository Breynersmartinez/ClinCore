package com.example.citas_medicas_api.role.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class RolRequest {
    @NotBlank
    @Size(max = 50)
    private String nombreRol;

    @Size(max = 255)
    private String descripcion;

    private Set<Long> permisoIds;
}

