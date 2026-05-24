package com.example.citas_medicas_api.doctor.model;

import com.example.citas_medicas_api.person.model.Persona;
import com.example.citas_medicas_api.specialty.model.Especialidad;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MEDICOS", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long idMedico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERSONA", nullable = false, unique = true)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESPECIALIDAD", nullable = false)
    private Especialidad especialidad;

    @Column(name = "NUMERO_REGISTRO", length = 20, nullable = false, unique = true)
    private String numeroRegistro;

    @Column(name = "TARIFA_CONSULTA", precision = 12, scale = 2)
    private BigDecimal tarifaConsulta;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;
}