package com.example.citas_medicas_api.department.model;

import com.example.citas_medicas_api.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "DEPARTAMENTOS", schema = "APP_CITAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Departamento extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEPARTAMENTO")
    private Long idDepartamento;

    @Column(name = "CODIGO_DANE", length = 2, nullable = false, unique = true)
    private String codigoDane;

    @Column(name = "NOMBRE_DEPARTAMENTO", length = 100, nullable = false, unique = true)
    private String nombreDepartamento;
}
