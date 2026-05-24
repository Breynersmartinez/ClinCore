package com.example.citas_medicas_api.person.repository;

import com.example.citas_medicas_api.person.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
