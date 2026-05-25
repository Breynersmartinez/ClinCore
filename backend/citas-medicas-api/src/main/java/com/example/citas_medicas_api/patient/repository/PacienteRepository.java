package com.example.citas_medicas_api.patient.repository;

import com.example.citas_medicas_api.patient.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Override
    @Query("SELECT p FROM Paciente p JOIN FETCH p.persona")
    List<Paciente> findAll();

    @Override
    @Query("SELECT p FROM Paciente p JOIN FETCH p.persona WHERE p.idPaciente = :id")
    Optional<Paciente> findById(@Param("id") Long id);

    @Query("SELECT p FROM Paciente p JOIN FETCH p.persona WHERE p.persona.idPersona = :idPersona")
    Optional<Paciente> findByPersona_IdPersona(@Param("idPersona") Long idPersona);

    Optional<Paciente> findByNumeroHistoria(String numeroHistoria);

    @Query("SELECT p FROM Paciente p JOIN FETCH p.persona WHERE p.activo = :activo")
    List<Paciente> findByActivo(@Param("activo") String activo);

    boolean existsByNumeroHistoria(String numeroHistoria);
}
