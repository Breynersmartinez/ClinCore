package com.example.citas_medicas_api.doctor.repository;

import com.example.citas_medicas_api.doctor.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByPersona_IdPersona(Long idPersona);
    List<Medico> findByEspecialidad_IdEspecialidad(Long idEspecialidad);
    List<Medico> findByActivo(String activo);
    boolean existsByNumeroRegistro(String numeroRegistro);

    @Query("SELECT m FROM Medico m WHERE m.especialidad.idEspecialidad = :idEsp AND m.activo = 'S'")
    List<Medico> findActivosByEspecialidad(Long idEsp);
}

