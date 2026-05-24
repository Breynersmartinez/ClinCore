package com.example.citas_medicas_api.person.model;


import com.example.citas_medicas_api.baseEntity.BaseEntity;
import com.example.citas_medicas_api.municipality.model.Municipio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PERSONAS", schema = "APP_CITAS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"TIPO_DOCUMENTO", "NUMERO_DOCUMENTO"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Persona extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERSONA")
    private Long idPersona;

    @Column(name = "TIPO_DOCUMENTO", length = 3, nullable = false)
    private String tipoDocumento; // CC, TI, CE, PAS, RC

    @Column(name = "NUMERO_DOCUMENTO", length = 20, nullable = false)
    private String numeroDocumento;

    @Column(name = "PRIMER_NOMBRE", length = 80, nullable = false)
    private String primerNombre;

    @Column(name = "SEGUNDO_NOMBRE", length = 80)
    private String segundoNombre;

    @Column(name = "PRIMER_APELLIDO", length = 80, nullable = false)
    private String primerApellido;

    @Column(name = "SEGUNDO_APELLIDO", length = 80)
    private String segundoApellido;

    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    @Column(name = "SEXO", length = 1)
    private String sexo; // M, F, O

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MUNICIPIO")
    private Municipio municipio;
}
