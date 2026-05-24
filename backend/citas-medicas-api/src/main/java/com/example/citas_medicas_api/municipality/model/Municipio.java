package com.example.citas_medicas_api.municipality.model;


import com.example.citas_medicas_api.baseEntity.BaseEntity;
import com.example.citas_medicas_api.department.model.Departamento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MUNICIPIOS", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Municipio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MUNICIPIO")
    private Long idMunicipio;

    @Column(name = "CODIGO_DANE", length = 5, nullable = false, unique = true)
    private String codigoDane;

    @Column(name = "NOMBRE_MUNICIPIO", length = 150, nullable = false)
    private String nombreMunicipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEPARTAMENTO", nullable = false)
    private Departamento departamento;
}
