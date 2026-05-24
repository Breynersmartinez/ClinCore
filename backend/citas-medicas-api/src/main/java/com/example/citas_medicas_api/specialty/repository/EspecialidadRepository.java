package com.example.citas_medicas_api.specialty.repository;

import com.example.citas_medicas_api.specialty.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    List<Especialidad> findByActivo(String activo);
    boolean existsByCodigoEspecialidad(String codigo);
}