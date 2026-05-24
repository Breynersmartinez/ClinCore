package com.example.citas_medicas_api.permission.repository;

import com.example.citas_medicas_api.permission.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombrePermiso(String nombrePermiso);
    List<Permiso> findByModulo(String modulo);
    List<Permiso> findByAccion(String accion);
    boolean existsByNombrePermiso(String nombrePermiso);
}
