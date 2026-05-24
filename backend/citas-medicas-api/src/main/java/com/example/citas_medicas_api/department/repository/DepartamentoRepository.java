package com.example.citas_medicas_api.department.repository;

import com.example.citas_medicas_api.department.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByCodigoDane(String codigoDane);
    List<Departamento> findByActivo(String activo);
    boolean existsByCodigoDane(String codigoDane);
    boolean existsByNombreDepartamento(String nombreDepartamento);
}
