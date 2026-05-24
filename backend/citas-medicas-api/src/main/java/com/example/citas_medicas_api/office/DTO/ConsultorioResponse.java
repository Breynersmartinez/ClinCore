package com.example.citas_medicas_api.office.DTO;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ConsultorioResponse {
    private Long idConsultorio;
    private String codigoConsultorio;
    private String nombreConsultorio;
    private Integer numeroPiso;
    private Integer capacidad;
    private String activo;
    private Long idSede;
    private String nombreSede;
    private String codigoSede;
}
