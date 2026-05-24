package com.example.citas_medicas_api.office.model;


import com.example.citas_medicas_api.baseEntity.BaseEntity;
import com.example.citas_medicas_api.headquarters.model.Sede;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "CONSULTORIOS", schema = "APP_CITAS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"CODIGO_CONSULTORIO", "ID_SEDE"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Consultorio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONSULTORIO")
    private Long idConsultorio;

    @Column(name = "CODIGO_CONSULTORIO", length = 10, nullable = false)
    private String codigoConsultorio;

    @Column(name = "NOMBRE_CONSULTORIO", length = 200, nullable = false)
    private String nombreConsultorio;

    @Column(name = "NUMERO_PISO", precision = 2)
    private Integer numeroPiso;

    @Column(name = "CAPACIDAD", precision = 3)
    private Integer capacidad = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SEDE", nullable = false)
    private Sede sede;
}
