package com.example.citas_medicas_api.doctor.repository;

import com.example.citas_medicas_api.doctor.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Override
    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad")
    List<Medico> findAll();

    @Override
    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad WHERE m.idMedico = :id")
    Optional<Medico> findById(@Param("id") Long id);

    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad WHERE m.persona.idPersona = :idPersona")
    Optional<Medico> findByPersona_IdPersona(@Param("idPersona") Long idPersona);

    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad WHERE m.especialidad.idEspecialidad = :idEspecialidad")
    List<Medico> findByEspecialidad_IdEspecialidad(@Param("idEspecialidad") Long idEspecialidad);

    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad WHERE m.activo = :activo")
    List<Medico> findByActivo(@Param("activo") String activo);

    boolean existsByNumeroRegistro(String numeroRegistro);

    @Query("SELECT m FROM Medico m JOIN FETCH m.persona JOIN FETCH m.especialidad WHERE m.especialidad.idEspecialidad = :idEsp AND m.activo = 'S'")
    List<Medico> findActivosByEspecialidad(@Param("idEsp") Long idEsp);
}
