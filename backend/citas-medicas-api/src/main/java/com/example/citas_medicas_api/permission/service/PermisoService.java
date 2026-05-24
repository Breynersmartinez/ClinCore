package com.example.citas_medicas_api.permission.service;

import com.example.citas_medicas_api.permission.DTO.PermisoResponseDTO;
import com.example.citas_medicas_api.permission.model.Permiso;
import com.example.citas_medicas_api.permission.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoService {

    private final PermisoRepository permisoRepository;

    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> listar(String modulo) {
        List<Permiso> permisos = StringUtils.hasText(modulo)
                ? permisoRepository.findByModulo(modulo.trim().toUpperCase())
                : permisoRepository.findAll();

        return permisos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PermisoResponseDTO toResponse(Permiso permiso) {
        return PermisoResponseDTO.builder()
                .idPermiso(permiso.getIdPermiso())
                .nombrePermiso(permiso.getNombrePermiso())
                .modulo(permiso.getModulo())
                .accion(permiso.getAccion())
                .descripcion(permiso.getDescripcion())
                .activo(permiso.getActivo())
                .build();
    }
}
