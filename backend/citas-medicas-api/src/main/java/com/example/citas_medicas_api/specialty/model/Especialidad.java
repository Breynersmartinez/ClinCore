package com.example.citas_medicas_api.specialty.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ESPECIALIDADES", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPECIALIDAD")
    private Long idEspecialidad;

    @Column(name = "CODIGO_ESPECIALIDAD", length = 10, nullable = false, unique = true)
    private String codigoEspecialidad;

    @Column(name = "NOMBRE_ESPECIALIDAD", length = 150, nullable = false)
    private String nombreEspecialidad;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;
}

