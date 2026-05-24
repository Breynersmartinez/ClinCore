package com.example.citas_medicas_api.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "AUDITORIA_ACCIONES", schema = "APP_CITAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditoriaAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA")
    private Long idAuditoria;

    @Column(name = "TABLA_AFECTADA", length = 50, nullable = false)
    private String tablaAfectada;

    @Column(name = "ACCION", length = 10, nullable = false)
    private String accion; // INSERT, UPDATE, DELETE, SELECT

    @Column(name = "ID_REGISTRO")
    private Long idRegistro;

    @Column(name = "USUARIO_BD", length = 50, nullable = false)
    private String usuarioBd;

    @Column(name = "ROL_ACTIVO", length = 50)
    private String rolActivo;

    @Column(name = "DATOS_ANTERIORES", length = 4000)
    private String datosAnteriores;

    @Column(name = "DATOS_NUEVOS", length = 4000)
    private String datosNuevos;

    @Column(name = "FECHA_ACCION")
    private LocalDate fechaAccion;

    @Column(name = "IP_CLIENTE", length = 50)
    private String ipCliente;
}
