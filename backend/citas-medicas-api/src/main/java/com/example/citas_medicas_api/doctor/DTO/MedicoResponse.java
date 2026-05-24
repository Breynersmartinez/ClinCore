package com.example.citas_medicas_api.doctor.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponse {
    private Long idMedico;
    private Long idPersona;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Long idEspecialidad;
    private String codigoEspecialidad;
    private String nombreEspecialidad;
    private String numeroRegistro;
    private BigDecimal tarifaConsulta;
    private String activo;
    private LocalDate fechaCreacion;
}
