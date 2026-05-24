package com.example.citas_medicas_api.headquarters.model;


import com.example.citas_medicas_api.baseEntity.BaseEntity;
import com.example.citas_medicas_api.municipality.model.Municipio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SEDES", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Sede extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SEDE")
    private Long idSede;

    @Column(name = "CODIGO_SEDE", length = 10, nullable = false, unique = true)
    private String codigoSede;

    @Column(name = "NOMBRE_SEDE", length = 200, nullable = false)
    private String nombreSede;

    @Column(name = "DIRECCION", length = 300, nullable = false)
    private String direccion;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MUNICIPIO", nullable = false)
    private Municipio municipio;
}
