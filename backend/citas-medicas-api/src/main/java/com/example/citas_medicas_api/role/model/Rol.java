package com.example.citas_medicas_api.role.model;


import com.example.citas_medicas_api.permission.model.Permiso;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Rol de la aplicación Spring Boot.
 * Mapea directamente con los roles de Oracle DB:
 * ROL_MEDICO, ROL_PACIENTE, ROL_ADMINISTRATIVO, ROL_AUXILIAR_MEDICO
 */
@Entity
@Table(name = "APP_ROLES", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long idRol;

    /**
     * Nombre del rol (debe coincidir con los roles Oracle DB).
     * Spring Security lo usa como: ROLE_MEDICO, ROLE_PACIENTE, etc.
     */
    @Column(name = "NOMBRE_ROL", length = 50, nullable = false, unique = true)
    private String nombreRol; // MEDICO, PACIENTE, ADMINISTRATIVO, AUXILIAR_MEDICO

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    /**
     * Permisos asignados a este rol (relación M:N)
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "APP_ROL_PERMISOS",
            schema = "APP_CITAS",
            joinColumns = @JoinColumn(name = "ID_ROL"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISO")
    )
    @Builder.Default
    private Set<Permiso> permisos = new HashSet<>();
}
