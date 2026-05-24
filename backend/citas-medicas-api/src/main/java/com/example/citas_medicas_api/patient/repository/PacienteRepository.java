package com.example.citas_medicas_api.patient.repository;

import com.example.citas_medicas_api.patient.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByPersona_IdPersona(Long idPersona);
    Optional<Paciente> findByNumeroHistoria(String numeroHistoria);
    List<Paciente> findByActivo(String activo);
    boolean existsByNumeroHistoria(String numeroHistoria);
}
