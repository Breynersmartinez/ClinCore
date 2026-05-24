package com.example.citas_medicas_api.office.repository;

import com.example.citas_medicas_api.office.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    List<Consultorio> findBySede_IdSede(Long idSede);
    List<Consultorio> findByActivo(String activo);
    boolean existsByCodigoConsultorioAndSede_IdSede(String codigo, Long idSede);

    @Query("SELECT c FROM Consultorio c WHERE c.sede.idSede = :idSede AND c.activo = 'S'")
    List<Consultorio> findActivosBySede(Long idSede);
}