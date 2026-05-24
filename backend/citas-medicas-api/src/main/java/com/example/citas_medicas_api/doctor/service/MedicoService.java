package com.example.citas_medicas_api.doctor.service;

import com.example.citas_medicas_api.doctor.DTO.MedicoRequest;
import com.example.citas_medicas_api.doctor.DTO.MedicoResponse;
import com.example.citas_medicas_api.doctor.model.Medico;
import com.example.citas_medicas_api.doctor.repository.MedicoRepository;
import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.person.model.Persona;
import com.example.citas_medicas_api.person.repository.PersonaRepository;
import com.example.citas_medicas_api.specialty.model.Especialidad;
import com.example.citas_medicas_api.specialty.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final PersonaRepository personaRepository;
    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicoResponse> listarActivos() {
        return medicoRepository.findByActivo("S").stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicoResponse> listarPorEspecialidad(Long idEspecialidad) {
        return medicoRepository.findByEspecialidad_IdEspecialidad(idEspecialidad).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicoResponse> listarActivosPorEspecialidad(Long idEspecialidad) {
        return medicoRepository.findActivosByEspecialidad(idEspecialidad).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicoResponse buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public MedicoResponse buscarPorPersona(Long idPersona) {
        return medicoRepository.findByPersona_IdPersona(idPersona)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Médico para persona", idPersona));
    }

    @Transactional
    public MedicoResponse crear(MedicoRequest request) {
        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new ResourceNotFoundException("Persona", request.getIdPersona()));
        Especialidad especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", request.getIdEspecialidad()));

        if (medicoRepository.findByPersona_IdPersona(request.getIdPersona()).isPresent()) {
            throw new BusinessException("La persona ya está registrada como médico");
        }

        String numeroRegistro = request.getNumeroRegistro().trim().toUpperCase();
        if (medicoRepository.existsByNumeroRegistro(numeroRegistro)) {
            throw new BusinessException("Ya existe un médico con número de registro: " + numeroRegistro);
        }

        Medico medico = Medico.builder()
                .persona(persona)
                .especialidad(especialidad)
                .numeroRegistro(numeroRegistro)
                .tarifaConsulta(request.getTarifaConsulta())
                .activo(defaultUpper(request.getActivo(), "S"))
                .fechaCreacion(LocalDate.now())
                .build();

        return toResponse(medicoRepository.save(medico));
    }

    @Transactional
    public MedicoResponse actualizar(Long id, MedicoRequest request) {
        Medico medico = findById(id);

        if (!medico.getPersona().getIdPersona().equals(request.getIdPersona())) {
            Persona persona = personaRepository.findById(request.getIdPersona())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona", request.getIdPersona()));
            if (medicoRepository.findByPersona_IdPersona(request.getIdPersona()).isPresent()) {
                throw new BusinessException("La persona ya está registrada como médico");
            }
            medico.setPersona(persona);
        }

        Especialidad especialidad = especialidadRepository.findById(request.getIdEspecialidad())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", request.getIdEspecialidad()));

        String numeroRegistro = request.getNumeroRegistro().trim().toUpperCase();
        if (!medico.getNumeroRegistro().equals(numeroRegistro)
                && medicoRepository.existsByNumeroRegistro(numeroRegistro)) {
            throw new BusinessException("Ya existe un médico con número de registro: " + numeroRegistro);
        }

        medico.setEspecialidad(especialidad);
        medico.setNumeroRegistro(numeroRegistro);
        medico.setTarifaConsulta(request.getTarifaConsulta());
        if (request.getActivo() != null) {
            medico.setActivo(request.getActivo().trim().toUpperCase());
        }

        return toResponse(medicoRepository.save(medico));
    }

    @Transactional
    public void eliminar(Long id) {
        Medico medico = findById(id);
        medico.setActivo("N");
        medicoRepository.save(medico);
    }

    private Medico findById(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", id));
    }

    private MedicoResponse toResponse(Medico medico) {
        Persona persona = medico.getPersona();
        Especialidad especialidad = medico.getEspecialidad();
        return MedicoResponse.builder()
                .idMedico(medico.getIdMedico())
                .idPersona(persona.getIdPersona())
                .tipoDocumento(persona.getTipoDocumento())
                .numeroDocumento(persona.getNumeroDocumento())
                .nombreCompleto(nombreCompleto(persona))
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .idEspecialidad(especialidad.getIdEspecialidad())
                .codigoEspecialidad(especialidad.getCodigoEspecialidad())
                .nombreEspecialidad(especialidad.getNombreEspecialidad())
                .numeroRegistro(medico.getNumeroRegistro())
                .tarifaConsulta(medico.getTarifaConsulta())
                .activo(medico.getActivo())
                .fechaCreacion(medico.getFechaCreacion())
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
}
