package com.example.citas_medicas_api.doctor.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRequest {

    @NotNull(message = "La persona es requerida")
    private Long idPersona;

    @NotNull(message = "La especialidad es requerida")
    private Long idEspecialidad;

    @NotBlank(message = "El número de registro es requerido")
    @Size(max = 20)
    private String numeroRegistro;

    @DecimalMin(value = "0.0", inclusive = false, message = "La tarifa debe ser mayor a cero")
    private BigDecimal tarifaConsulta;

    @Size(max = 1)
    private String activo;
}
