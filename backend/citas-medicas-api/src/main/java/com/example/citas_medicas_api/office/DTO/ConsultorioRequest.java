package com.example.citas_medicas_api.office.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ConsultorioRequest {

    @NotBlank(message = "El código del consultorio es requerido")
    @Size(max = 10)
    private String codigoConsultorio;

    @NotBlank(message = "El nombre del consultorio es requerido")
    @Size(max = 200)
    private String nombreConsultorio;

    @Min(value = 1, message = "El piso debe ser mayor a 0")
    private Integer numeroPiso;

    @Min(value = 1, message = "La capacidad mínima es 1")
    @Max(value = 50, message = "La capacidad máxima es 50")
    private Integer capacidad = 1;

    @NotNull(message = "La sede es requerida")
    private Long idSede;

    private String activo = "S";
}
