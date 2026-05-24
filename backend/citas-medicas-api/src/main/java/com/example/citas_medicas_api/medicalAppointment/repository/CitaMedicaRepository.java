package com.example.citas_medicas_api.medicalAppointment.repository;

import com.example.citas_medicas_api.medicalAppointment.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
    Optional<CitaMedica> findByNumeroCita(String numeroCita);
    List<CitaMedica> findByPaciente_IdPaciente(Long idPaciente);
    List<CitaMedica> findByMedico_IdMedico(Long idMedico);
    List<CitaMedica> findByFechaCitaAndMedico_IdMedico(LocalDate fecha, Long idMedico);
    List<CitaMedica> findByEstado(String estado);

    @Query("SELECT c FROM CitaMedica c WHERE c.medico.idMedico = :idMedico " +
            "AND c.fechaCita = :fecha AND c.estado NOT IN ('CANCELADA') " +
            "AND ((c.horaInicio <= :horaFin) AND (c.horaFin >= :horaInicio))")
    List<CitaMedica> findSolapadas(Long idMedico, LocalDate fecha,
                                   String horaInicio, String horaFin);
}
