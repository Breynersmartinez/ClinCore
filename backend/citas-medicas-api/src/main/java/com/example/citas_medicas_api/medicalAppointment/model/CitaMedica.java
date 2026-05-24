package com.example.citas_medicas_api.medicalAppointment.model;


import com.example.citas_medicas_api.baseEntity.BaseEntity;
import com.example.citas_medicas_api.doctor.model.Medico;
import com.example.citas_medicas_api.office.model.Consultorio;
import com.example.citas_medicas_api.patient.model.Paciente;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "CITAS_MEDICAS", schema = "APP_CITAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CitaMedica extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long idCita;

    @Column(name = "NUMERO_CITA", length = 20, nullable = false, unique = true)
    private String numeroCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONSULTORIO", nullable = false)
    private Consultorio consultorio;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "HORA_INICIO", length = 5, nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", length = 5, nullable = false)
    private String horaFin;

    @Column(name = "ESTADO", length = 20)
    private String estado = "PROGRAMADA"; // PROGRAMADA, CONFIRMADA, ATENDIDA, CANCELADA, NO_ASISTIO

    @Column(name = "MOTIVO_CONSULTA", length = 500)
    private String motivoConsulta;

    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;
}

