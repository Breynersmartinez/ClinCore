package com.example.citas_medicas_api.patient.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    @NotNull(message = "La persona es requerida")
    private Long idPersona;

    @NotBlank(message = "El número de historia es requerido")
    @Size(max = 20)
    private String numeroHistoria;

    @Size(max = 20)
    private String tipoAfiliacion;

    @Size(max = 100)
    private String eps;

    @Size(max = 1)
    private String activo;
}
