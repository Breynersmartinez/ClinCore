package com.example.citas_medicas_api.user.model;


import com.example.citas_medicas_api.person.model.Persona;
import com.example.citas_medicas_api.role.model.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Usuario del sistema para autenticación JWT.
 * Cada usuario puede tener uno o varios roles.
 * El campo username puede coincidir con los usr_medico, usr_paciente, etc. de Oracle.
 */
@Entity
@Table(name = "APP_USUARIOS", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "USERNAME", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD_HASH", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ACTIVO", length = 1)
    private String activo = "S";

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "ULTIMO_ACCESO")
    private LocalDate ultimoAcceso;

    /**
     * Relación con Persona (opcional: el usuario puede estar asociado a una persona).
     * Un médico o paciente tendrá un usuario de login.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERSONA")
    private Persona persona;

    /**
     * Roles asignados (M:N)
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "APP_USUARIO_ROLES",
            schema = "APP_CITAS",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROL")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    public boolean isActivo() {
        return "S".equals(activo);
    }
}