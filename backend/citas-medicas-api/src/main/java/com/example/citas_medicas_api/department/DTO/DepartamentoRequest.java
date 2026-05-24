package com.example.citas_medicas_api.department.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DepartamentoRequest {
    @NotBlank @Size(min = 2, max = 2, message = "El código DANE debe tener 2 dígitos")
    private String codigoDane;

    @NotBlank @Size(max = 100)
    private String nombreDepartamento;

    private String activo = "S";
}