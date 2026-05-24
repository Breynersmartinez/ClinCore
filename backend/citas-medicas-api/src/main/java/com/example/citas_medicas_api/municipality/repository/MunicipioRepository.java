package com.example.citas_medicas_api.municipality.repository;

import com.example.citas_medicas_api.municipality.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    List<Municipio> findByDepartamento_IdDepartamento(Long idDepartamento);
    List<Municipio> findByActivo(String activo);
    Optional<Municipio> findByCodigoDane(String codigoDane);
}
