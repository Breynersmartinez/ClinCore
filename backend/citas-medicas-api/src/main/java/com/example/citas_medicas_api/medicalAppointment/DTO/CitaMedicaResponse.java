package com.example.citas_medicas_api.medicalAppointment.DTO;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CitaMedicaResponse {
    private Long idCita;
    private String numeroCita;
    private LocalDate fechaCita;
    private String horaInicio;
    private String horaFin;
    private String estado;
    private String motivoConsulta;
    private String observaciones;

    // Paciente
    private Long idPaciente;
    private String nombrePaciente;
    private String documentoPaciente;

    // Médico
    private Long idMedico;
    private String nombreMedico;
    private String especialidad;

    // Consultorio
    private Long idConsultorio;
    private String nombreConsultorio;
    private String nombreSede;
}