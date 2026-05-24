package com.example.citas_medicas_api.baseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Column(name = "ACTIVO", length = 1, nullable = false)
    private String activo = "S";

    @CreatedDate
    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @LastModifiedDate
    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @CreatedBy
    @Column(name = "USUARIO_CREACION", length = 50, nullable = false, updatable = false)
    private String usuarioCreacion;
}
