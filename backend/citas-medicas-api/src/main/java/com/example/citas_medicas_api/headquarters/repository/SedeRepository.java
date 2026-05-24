package com.example.citas_medicas_api.headquarters.repository;

import com.example.citas_medicas_api.headquarters.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    List<Sede> findByActivo(String activo);
    List<Sede> findByMunicipio_IdMunicipio(Long idMunicipio);
    boolean existsByCodigoSede(String codigoSede);
}
