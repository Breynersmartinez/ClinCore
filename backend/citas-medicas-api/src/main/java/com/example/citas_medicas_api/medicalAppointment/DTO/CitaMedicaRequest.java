package com.example.citas_medicas_api.medicalAppointment.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CitaMedicaRequest {

    @NotNull(message = "El paciente es requerido")
    private Long idPaciente;

    @NotNull(message = "El médico es requerido")
    private Long idMedico;

    @NotNull(message = "El consultorio es requerido")
    private Long idConsultorio;

    @NotNull(message = "La fecha de la cita es requerida")
    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    private LocalDate fechaCita;

    @NotBlank(message = "La hora de inicio es requerida")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Formato HH:mm requerido")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es requerida")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Formato HH:mm requerido")
    private String horaFin;

    @Size(max = 500)
    private String motivoConsulta;

    @Size(max = 1000)
    private String observaciones;
}