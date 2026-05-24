package com.example.citas_medicas_api.patient.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.patient.DTO.PacienteRequest;
import com.example.citas_medicas_api.patient.DTO.PacienteResponse;
import com.example.citas_medicas_api.patient.model.Paciente;
import com.example.citas_medicas_api.patient.repository.PacienteRepository;
import com.example.citas_medicas_api.person.model.Persona;
import com.example.citas_medicas_api.person.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarActivos() {
        return pacienteRepository.findByActivo("S").stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorPersona(Long idPersona) {
        return pacienteRepository.findByPersona_IdPersona(idPersona)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente para persona", idPersona));
    }

    @Transactional
    public PacienteResponse crear(PacienteRequest request) {
        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", request.getIdPersona()));

        if (pacienteRepository.findByPersona_IdPersona(request.getIdPersona()).isPresent()) {
            throw new BusinessException("La persona ya está registrada como paciente");
        }

        String numeroHistoria = request.getNumeroHistoria().trim().toUpperCase();
        if (pacienteRepository.existsByNumeroHistoria(numeroHistoria)) {
            throw new BusinessException("Ya existe un paciente con número de historia: " + numeroHistoria);
        }

        Paciente paciente = Paciente.builder()
                .persona(persona)
                .numeroHistoria(numeroHistoria)
                .tipoAfiliacion(defaultUpper(request.getTipoAfiliacion(), "CONTRIBUTIVO"))
                .eps(upperOrNull(request.getEps()))
                .activo(defaultUpper(request.getActivo(), "S"))
                .fechaCreacion(LocalDate.now())
                .build();

        return toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public PacienteResponse actualizar(Long id, PacienteRequest request) {
        Paciente paciente = findById(id);

        if (!paciente.getPersona().getIdPersona().equals(request.getIdPersona())) {
            Persona persona = personaRepository.findById(request.getIdPersona())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", request.getIdPersona()));

            if (pacienteRepository.findByPersona_IdPersona(request.getIdPersona()).isPresent()) {
                throw new BusinessException("La persona ya está registrada como paciente");
            }
            paciente.setPersona(persona);
        }

        String numeroHistoria = request.getNumeroHistoria().trim().toUpperCase();
        if (!paciente.getNumeroHistoria().equals(numeroHistoria)
                && pacienteRepository.existsByNumeroHistoria(numeroHistoria)) {
            throw new BusinessException("Ya existe un paciente con número de historia: " + numeroHistoria);
        }

        paciente.setNumeroHistoria(numeroHistoria);
        paciente.setTipoAfiliacion(defaultUpper(request.getTipoAfiliacion(), "CONTRIBUTIVO"));
        paciente.setEps(upperOrNull(request.getEps()));
        if (request.getActivo() != null) {
            paciente.setActivo(request.getActivo().trim().toUpperCase());
        }

        return toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = findById(id);
        paciente.setActivo("N");
        pacienteRepository.save(paciente);
    }

    private Paciente findById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
    }

    private PacienteResponse toResponse(Paciente paciente) {
        Persona persona = paciente.getPersona();
        return PacienteResponse.builder()
                .idPaciente(paciente.getIdPaciente())
                .idPersona(persona.getIdPersona())
                .tipoDocumento(persona.getTipoDocumento())
                .numeroDocumento(persona.getNumeroDocumento())
                .nombreCompleto(nombreCompleto(persona))
                .fechaNacimiento(persona.getFechaNacimiento())
                .sexo(persona.getSexo())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .numeroHistoria(paciente.getNumeroHistoria())
                .tipoAfiliacion(paciente.getTipoAfiliacion())
                .eps(paciente.getEps())
                .activo(paciente.getActivo())
                .fechaCreacion(paciente.getFechaCreacion())
                .build();
    }

    private String nombreCompleto(Persona persona) {
        return String.join(" ",
                persona.getPrimerNombre(),
                persona.getSegundoNombre() != null ? persona.getSegundoNombre() : "",
                persona.getPrimerApellido(),
                persona.getSegundoApellido() != null ? persona.getSegundoApellido() : "").trim()
                .replaceAll("\\s+", " ");
    }

    private String defaultUpper(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim().toUpperCase();
    }

    private String upperOrNull(String value) {
        return value == null || value.isBlank() ? null : value.trim().toUpperCase();
    }
}
