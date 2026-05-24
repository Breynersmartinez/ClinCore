package com.example.citas_medicas_api.patient.model;

import com.example.citas_medicas_api.person.model.Persona;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PACIENTES", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERSONA", nullable = false, unique = true)
    private Persona persona;

    @Column(name = "NUMERO_HISTORIA", length = 20, nullable = false, unique = true)
    private String numeroHistoria;

    @Column(name = "TIPO_AFILIACION", length = 20)
    private String tipoAfiliacion = "CONTRIBUTIVO"; // CONTRIBUTIVO, SUBSIDIADO, VINCULADO, PARTICULAR

    @Column(name = "EPS", length = 100)
    private String eps;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;
}