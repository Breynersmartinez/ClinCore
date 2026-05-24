package com.example.citas_medicas_api.headquarters.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.headquarters.DTO.SedeRequest;
import com.example.citas_medicas_api.headquarters.DTO.SedeResponse;
import com.example.citas_medicas_api.headquarters.model.Sede;
import com.example.citas_medicas_api.headquarters.repository.SedeRepository;
import com.example.citas_medicas_api.municipality.model.Municipio;
import com.example.citas_medicas_api.municipality.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SedeService {

    private final SedeRepository sedeRepository;
    private final MunicipioRepository municipioRepository;

    @Transactional(readOnly = true)
    public List<SedeResponse> listarTodas() {
        return sedeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SedeResponse> listarActivas() {
        return sedeRepository.findByActivo("S").stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SedeResponse buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public SedeResponse crear(SedeRequest request) {
        if (sedeRepository.existsByCodigoSede(request.getCodigoSede()))
            throw new BusinessException("Ya existe una sede con código: " + request.getCodigoSede());

        Municipio municipio = municipioRepository.findById(request.getIdMunicipio())
                .orElseThrow(() -> new ResourceNotFoundException("Municipio", request.getIdMunicipio()));

        Sede sede = Sede.builder()
                .codigoSede(request.getCodigoSede().toUpperCase())
                .nombreSede(request.getNombreSede().toUpperCase())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .municipio(municipio)
                .activo("S")
                .build();
        return toResponse(sedeRepository.save(sede));
    }

    @Transactional
    public SedeResponse actualizar(Long id, SedeRequest request) {
        Sede sede = findById(id);
        Municipio municipio = municipioRepository.findById(request.getIdMunicipio())
                .orElseThrow(() -> new ResourceNotFoundException("Municipio", request.getIdMunicipio()));
        sede.setNombreSede(request.getNombreSede().toUpperCase());
        sede.setDireccion(request.getDireccion());
        sede.setTelefono(request.getTelefono());
        sede.setEmail(request.getEmail());
        sede.setMunicipio(municipio);
        if (request.getActivo() != null) sede.setActivo(request.getActivo());
        return toResponse(sedeRepository.save(sede));
    }

    @Transactional
    public void eliminar(Long id) {
        Sede sede = findById(id);
        sede.setActivo("N");
        sedeRepository.save(sede);
    }

    private Sede findById(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", id));
    }

    private SedeResponse toResponse(Sede sede) {
        Municipio municipio = sede.getMunicipio();
        return SedeResponse.builder()
                .idSede(sede.getIdSede())
                .codigoSede(sede.getCodigoSede())
                .nombreSede(sede.getNombreSede())
                .direccion(sede.getDireccion())
                .telefono(sede.getTelefono())
                .email(sede.getEmail())
                .activo(sede.getActivo())
                .fechaCreacion(sede.getFechaCreacion())
                .idMunicipio(municipio != null ? municipio.getIdMunicipio() : null)
                .nombreMunicipio(municipio != null ? municipio.getNombreMunicipio() : null)
                .build();
    }
}
