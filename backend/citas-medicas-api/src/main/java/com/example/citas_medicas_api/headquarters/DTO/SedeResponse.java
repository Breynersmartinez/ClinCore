package com.example.citas_medicas_api.headquarters.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SedeResponse {
    private Long idSede;
    private String codigoSede;
    private String nombreSede;
    private String direccion;
    private String telefono;
    private String email;
    private String activo;
    private LocalDate fechaCreacion;
    private Long idMunicipio;
    private String nombreMunicipio;
}
