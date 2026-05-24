package com.example.citas_medicas_api.headquarters.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SedeRequest {
    @NotBlank @Size(max = 10)
    private String codigoSede;

    @NotBlank @Size(max = 200)
    private String nombreSede;

    @NotBlank @Size(max = 300)
    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Email @Size(max = 100)
    private String email;

    @NotNull
    private Long idMunicipio;

    private String activo = "S";
}