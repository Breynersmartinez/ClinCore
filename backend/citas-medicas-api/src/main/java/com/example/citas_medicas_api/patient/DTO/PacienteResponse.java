package com.example.citas_medicas_api.patient.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {
    private Long idPaciente;
    private Long idPersona;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String email;
    private String telefono;
    private String numeroHistoria;
    private String tipoAfiliacion;
    private String eps;
    private String activo;
    private LocalDate fechaCreacion;
}
