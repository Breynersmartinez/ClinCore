package com.example.citas_medicas_api.permission.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Permiso de acceso a nivel de aplicación.
 * Controla qué operaciones (CREATE, READ, UPDATE, DELETE) puede realizar
 * cada rol sobre cada módulo del sistema.
 *
 * NOTA: Esta tabla complementa los privilegios de Oracle DB con control
 * a nivel de aplicación Spring Boot.
 */
@Entity
@Table(name = "APP_PERMISOS", schema = "APP_CITAS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOMBRE_PERMISO"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISO")
    private Long idPermiso;

    /**
     * Nombre único del permiso en formato: MODULO_ACCION
     * Ej: CONSULTORIOS_CREATE, CITAS_READ, PACIENTES_UPDATE
     */
    @Column(name = "NOMBRE_PERMISO", length = 100, nullable = false)
    private String nombrePermiso;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "MODULO", length = 50)
    private String modulo; // CONSULTORIOS, CITAS, PACIENTES, etc.

    @Column(name = "ACCION", length = 10)
    private String accion; // CREATE, READ, UPDATE, DELETE

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";
}
