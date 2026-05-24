package com.example.citas_medicas_api.medicalAppointment.service;

import com.example.citas_medicas_api.doctor.model.Medico;
import com.example.citas_medicas_api.doctor.repository.MedicoRepository;
import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.medicalAppointment.DTO.CitaMedicaRequest;
import com.example.citas_medicas_api.medicalAppointment.DTO.CitaMedicaResponse;
import com.example.citas_medicas_api.medicalAppointment.model.CitaMedica;
import com.example.citas_medicas_api.medicalAppointment.repository.CitaMedicaRepository;
import com.example.citas_medicas_api.office.model.Consultorio;
import com.example.citas_medicas_api.office.repository.ConsultorioRepository;
import com.example.citas_medicas_api.patient.model.Paciente;
import com.example.citas_medicas_api.patient.repository.PacienteRepository;
import com.example.citas_medicas_api.person.model.Persona;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitaMedicaService {

    private final CitaMedicaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultorioRepository consultorioRepository;

    @Transactional(readOnly = true)
    public List<CitaMedicaResponse> listarTodas() {
        return citaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaMedicaResponse> listarPorPaciente(Long idPaciente) {
        return citaRepository.findByPaciente_IdPaciente(idPaciente).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaMedicaResponse> listarPorMedico(Long idMedico) {
        return citaRepository.findByMedico_IdMedico(idMedico).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CitaMedicaResponse buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public CitaMedicaResponse crear(CitaMedicaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", request.getIdPaciente()));
        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Médico", request.getIdMedico()));
        Consultorio consultorio = consultorioRepository.findById(request.getIdConsultorio())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio", request.getIdConsultorio()));

        // Validar solapamiento de horario del médico
        List<CitaMedica> solapadas = citaRepository.findSolapadas(
                request.getIdMedico(),
                request.getFechaCita(),
                request.getHoraInicio(),
                request.getHoraFin());
        if (!solapadas.isEmpty()) {
            throw new BusinessException("El médico ya tiene una cita en ese horario: "
                    + request.getHoraInicio() + " - " + request.getHoraFin());
        }

        // Validar que médico esté activo
        if (!"S".equals(medico.getActivo())) {
            throw new BusinessException("El médico seleccionado no está activo");
        }

        // Validar que consultorio esté activo
        if (!"S".equals(consultorio.getActivo())) {
            throw new BusinessException("El consultorio seleccionado no está disponible");
        }

        String numeroCita = generarNumeroCita();

        CitaMedica cita = CitaMedica.builder()
                .numeroCita(numeroCita)
                .paciente(paciente)
                .medico(medico)
                .consultorio(consultorio)
                .fechaCita(request.getFechaCita())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .estado("PROGRAMADA")
                .motivoConsulta(request.getMotivoConsulta())
                .observaciones(request.getObservaciones())
                .activo("S")
                .build();

        CitaMedica saved = citaRepository.save(cita);
        log.info("Cita creada: {} para paciente id={}", numeroCita, request.getIdPaciente());
        return toResponse(saved);
    }

    @Transactional
    public CitaMedicaResponse actualizarEstado(Long id, String nuevoEstado, String observaciones) {
        CitaMedica cita = findById(id);

        validarTransicionEstado(cita.getEstado(), nuevoEstado);

        cita.setEstado(nuevoEstado);
        if (observaciones != null && !observaciones.isBlank()) {
            cita.setObservaciones(observaciones);
        }

        return toResponse(citaRepository.save(cita));
    }

    @Transactional
    public CitaMedicaResponse actualizar(Long id, CitaMedicaRequest request) {
        CitaMedica cita = findById(id);

        // Solo se puede modificar si está PROGRAMADA o CONFIRMADA
        if (!List.of("PROGRAMADA", "CONFIRMADA").contains(cita.getEstado())) {
            throw new BusinessException("Solo se pueden modificar citas en estado PROGRAMADA o CONFIRMADA");
        }

        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", request.getIdPaciente()));
        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new ResourceNotFoundException("Médico", request.getIdMedico()));
        Consultorio consultorio = consultorioRepository.findById(request.getIdConsultorio())
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio", request.getIdConsultorio()));

        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setConsultorio(consultorio);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(request.getHoraFin());
        cita.setMotivoConsulta(request.getMotivoConsulta());
        cita.setObservaciones(request.getObservaciones());

        return toResponse(citaRepository.save(cita));
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        CitaMedica cita = findById(id);
        if (List.of("ATENDIDA", "CANCELADA").contains(cita.getEstado())) {
            throw new BusinessException("No se puede cancelar una cita en estado: " + cita.getEstado());
        }
        cita.setEstado("CANCELADA");
        cita.setObservaciones("CANCELADA: " + motivo);
        citaRepository.save(cita);
    }

    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        boolean valido = switch (estadoActual) {
            case "PROGRAMADA" -> List.of("CONFIRMADA", "CANCELADA", "NO_ASISTIO").contains(nuevoEstado);
            case "CONFIRMADA"  -> List.of("ATENDIDA", "CANCELADA", "NO_ASISTIO").contains(nuevoEstado);
            default -> false;
        };
        if (!valido) {
            throw new BusinessException("Transición de estado no permitida: "
                    + estadoActual + " → " + nuevoEstado);
        }
    }

    private String generarNumeroCita() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = citaRepository.count() + 1;
        return "CITA-" + fecha + "-" + String.format("%05d", count);
    }

    private CitaMedica findById(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita médica", id));
    }

    private CitaMedicaResponse toResponse(CitaMedica c) {
        Persona pp = c.getPaciente().getPersona();
        Persona pm = c.getMedico().getPersona();
        return CitaMedicaResponse.builder()
                .idCita(c.getIdCita())
                .numeroCita(c.getNumeroCita())
                .fechaCita(c.getFechaCita())
                .horaInicio(c.getHoraInicio())
                .horaFin(c.getHoraFin())
                .estado(c.getEstado())
                .motivoConsulta(c.getMotivoConsulta())
                .observaciones(c.getObservaciones())
                // Paciente
                .idPaciente(c.getPaciente().getIdPaciente())
                .nombrePaciente(pp.getPrimerNombre() + " " + pp.getPrimerApellido())
                .documentoPaciente(pp.getTipoDocumento() + " " + pp.getNumeroDocumento())
                // Médico
                .idMedico(c.getMedico().getIdMedico())
                .nombreMedico("Dr(a). " + pm.getPrimerNombre() + " " + pm.getPrimerApellido())
                .especialidad(c.getMedico().getEspecialidad().getNombreEspecialidad())
                // Consultorio
                .idConsultorio(c.getConsultorio().getIdConsultorio())
                .nombreConsultorio(c.getConsultorio().getNombreConsultorio())
                .nombreSede(c.getConsultorio().getSede().getNombreSede())
                .build();
    }
}