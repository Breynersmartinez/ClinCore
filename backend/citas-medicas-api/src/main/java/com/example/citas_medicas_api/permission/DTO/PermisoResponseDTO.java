package com.example.citas_medicas_api.permission.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermisoResponseDTO {

    private Long idPermiso;
    private String nombrePermiso;
    private String modulo;
    private String accion;
    private String descripcion;
    private String activo;
}