package com.example.citas_medicas_api.office.service;
import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.headquarters.model.Sede;
import com.example.citas_medicas_api.headquarters.repository.SedeRepository;
import com.example.citas_medicas_api.office.DTO.ConsultorioRequest;
import com.example.citas_medicas_api.office.DTO.ConsultorioResponse;
import com.example.citas_medicas_api.office.model.Consultorio;
import com.example.citas_medicas_api.office.repository.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final SedeRepository sedeRepository;

    @Transactional(readOnly = true)
    public List<ConsultorioResponse> listarTodos() {
        return consultorioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultorioResponse> listarPorSede(Long idSede) {
        return consultorioRepository.findBySede_IdSede(idSede).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultorioResponse> listarActivosPorSede(Long idSede) {
        return consultorioRepository.findActivosBySede(idSede).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConsultorioResponse buscarPorId(Long id) {
        return toResponse(findConsultorioById(id));
    }

    @Transactional
    public ConsultorioResponse crear(ConsultorioRequest request) {
        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new ResourceNotFoundException("Sede", request.getIdSede()));

        if (consultorioRepository.existsByCodigoConsultorioAndSede_IdSede(
                request.getCodigoConsultorio(), request.getIdSede())) {
            throw new BusinessException("Ya existe un consultorio con el código '"
                    + request.getCodigoConsultorio() + "' en esta sede");
        }

        Consultorio consultorio = Consultorio.builder()
                .codigoConsultorio(request.getCodigoConsultorio().toUpperCase())
                .nombreConsultorio(request.getNombreConsultorio().toUpperCase())
                .numeroPiso(request.getNumeroPiso())
                .capacidad(request.getCapacidad())
                .sede(sede)
                .activo(request.getActivo() != null ? request.getActivo() : "S")
                .build();

        return toResponse(consultorioRepository.save(consultorio));
    }

    @Transactional
    public ConsultorioResponse actualizar(Long id, ConsultorioRequest request) {
        Consultorio consultorio = findConsultorioById(id);
        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new ResourceNotFoundException("Sede", request.getIdSede()));

        // Validar unicidad solo si cambió código o sede
        boolean codigoCambio = !consultorio.getCodigoConsultorio()
                .equals(request.getCodigoConsultorio());
        boolean sedeCambio = !consultorio.getSede().getIdSede()
                .equals(request.getIdSede());

        if ((codigoCambio || sedeCambio) &&
                consultorioRepository.existsByCodigoConsultorioAndSede_IdSede(
                        request.getCodigoConsultorio(), request.getIdSede())) {
            throw new BusinessException("Ya existe un consultorio con ese código en la sede");
        }

        consultorio.setCodigoConsultorio(request.getCodigoConsultorio().toUpperCase());
        consultorio.setNombreConsultorio(request.getNombreConsultorio().toUpperCase());
        consultorio.setNumeroPiso(request.getNumeroPiso());
        consultorio.setCapacidad(request.getCapacidad());
        consultorio.setSede(sede);
        if (request.getActivo() != null) {
            consultorio.setActivo(request.getActivo());
        }

        return toResponse(consultorioRepository.save(consultorio));
    }

    @Transactional
    public void inactivar(Long id) {
        Consultorio consultorio = findConsultorioById(id);
        consultorio.setActivo("N");
        consultorioRepository.save(consultorio);
    }

    @Transactional
    public void eliminar(Long id) {
        Consultorio consultorio = findConsultorioById(id);
        // Eliminación lógica
        consultorio.setActivo("N");
        consultorioRepository.save(consultorio);
    }

    private Consultorio findConsultorioById(Long id) {
        return consultorioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio", id));
    }

    private ConsultorioResponse toResponse(Consultorio c) {
        return ConsultorioResponse.builder()
                .idConsultorio(c.getIdConsultorio())
                .codigoConsultorio(c.getCodigoConsultorio())
                .nombreConsultorio(c.getNombreConsultorio())
                .numeroPiso(c.getNumeroPiso())
                .capacidad(c.getCapacidad())
                .activo(c.getActivo())
                .idSede(c.getSede().getIdSede())
                .codigoSede(c.getSede().getCodigoSede())
                .nombreSede(c.getSede().getNombreSede())
                .build();
    }
}