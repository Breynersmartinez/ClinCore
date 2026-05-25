package com.example.citas_medicas_api.medicalAppointment.repository;

import com.example.citas_medicas_api.medicalAppointment.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    @Override
    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            """)
    List<CitaMedica> findAll();

    @Override
    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            WHERE c.idCita = :id
            """)
    Optional<CitaMedica> findById(@Param("id") Long id);

    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            WHERE c.numeroCita = :numeroCita
            """)
    Optional<CitaMedica> findByNumeroCita(@Param("numeroCita") String numeroCita);

    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            WHERE c.paciente.idPaciente = :idPaciente
            """)
    List<CitaMedica> findByPaciente_IdPaciente(@Param("idPaciente") Long idPaciente);

    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            WHERE c.medico.idMedico = :idMedico
            """)
    List<CitaMedica> findByMedico_IdMedico(@Param("idMedico") Long idMedico);

    List<CitaMedica> findByFechaCitaAndMedico_IdMedico(LocalDate fecha, Long idMedico);

    @Query("""
            SELECT c FROM CitaMedica c
            JOIN FETCH c.paciente p
            JOIN FETCH p.persona
            JOIN FETCH c.medico m
            JOIN FETCH m.persona
            JOIN FETCH m.especialidad
            JOIN FETCH c.consultorio co
            JOIN FETCH co.sede
            WHERE c.estado = :estado
            """)
    List<CitaMedica> findByEstado(@Param("estado") String estado);

    @Query("SELECT c FROM CitaMedica c WHERE c.medico.idMedico = :idMedico " +
            "AND c.fechaCita = :fecha AND c.estado NOT IN ('CANCELADA') " +
            "AND ((c.horaInicio <= :horaFin) AND (c.horaFin >= :horaInicio))")
    List<CitaMedica> findSolapadas(@Param("idMedico") Long idMedico,
                                   @Param("fecha") LocalDate fecha,
                                   @Param("horaInicio") String horaInicio,
                                   @Param("horaFin") String horaFin);
}
