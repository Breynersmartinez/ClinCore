package com.example.citas_medicas_api.department.service;

import com.example.citas_medicas_api.department.DTO.DepartamentoRequest;
import com.example.citas_medicas_api.department.model.Departamento;
import com.example.citas_medicas_api.department.repository.DepartamentoRepository;
import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository repo;

    @Transactional(readOnly = true)
    public List<Departamento> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Departamento> listarActivos() {
        return repo.findByActivo("S");
    }

    @Transactional(readOnly = true)
    public Departamento buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
    }

    @Transactional
    public Departamento crear(DepartamentoRequest request) {
        if (repo.existsByCodigoDane(request.getCodigoDane()))
            throw new BusinessException("Ya existe un departamento con código DANE: " + request.getCodigoDane());
        if (repo.existsByNombreDepartamento(request.getNombreDepartamento().toUpperCase()))
            throw new BusinessException("Ya existe un departamento con nombre: " + request.getNombreDepartamento());

        Departamento d = Departamento.builder()
                .codigoDane(request.getCodigoDane())
                .nombreDepartamento(request.getNombreDepartamento().toUpperCase())
                .activo("S")
                .build();
        return repo.save(d);
    }

    @Transactional
    public Departamento actualizar(Long id, DepartamentoRequest request) {
        Departamento d = buscarPorId(id);
        d.setNombreDepartamento(request.getNombreDepartamento().toUpperCase());
        if (request.getActivo() != null) d.setActivo(request.getActivo());
        return repo.save(d);
    }

    @Transactional
    public void eliminar(Long id) {
        Departamento d = buscarPorId(id);
        d.setActivo("N");
        repo.save(d);
    }
}

