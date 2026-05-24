package com.example.citas_medicas_api.role.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.permission.model.Permiso;
import com.example.citas_medicas_api.permission.repository.PermisoRepository;
import com.example.citas_medicas_api.role.DTO.RolRequest;
import com.example.citas_medicas_api.role.model.Rol;
import com.example.citas_medicas_api.role.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarTodos() {
        return rolRepository.findAll().stream().map(this::toMap).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarPorId(Long id) {
        return toMap(findById(id));
    }

    @Transactional
    public Map<String, Object> crear(RolRequest request) {
        if (rolRepository.existsByNombreRol(request.getNombreRol().toUpperCase())) {
            throw new BusinessException("El rol '" + request.getNombreRol() + "' ya existe");
        }
        Set<Permiso> permisos = resolverPermisos(request.getPermisoIds());

        Rol rol = Rol.builder()
                .nombreRol(request.getNombreRol().toUpperCase())
                .descripcion(request.getDescripcion())
                .activo("S")
                .permisos(permisos)
                .build();

        return toMap(rolRepository.save(rol));
    }

    @Transactional
    public Map<String, Object> actualizar(Long id, RolRequest request) {
        Rol rol = findById(id);
        rol.setDescripcion(request.getDescripcion());
        if (request.getPermisoIds() != null) {
            rol.setPermisos(resolverPermisos(request.getPermisoIds()));
        }
        return toMap(rolRepository.save(rol));
    }

    @Transactional
    public Map<String, Object> asignarPermisos(Long idRol, Set<Long> permisoIds) {
        Rol rol = findById(idRol);
        rol.setPermisos(resolverPermisos(permisoIds));
        return toMap(rolRepository.save(rol));
    }

    @Transactional
    public void inactivar(Long id) {
        Rol rol = findById(id);
        rol.setActivo("N");
        rolRepository.save(rol);
    }

    private Set<Permiso> resolverPermisos(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream()
                .map(pid -> permisoRepository.findById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Permiso", pid)))
                .collect(Collectors.toSet());
    }

    private Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));
    }

    private Map<String, Object> toMap(Rol r) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("idRol", r.getIdRol());
        map.put("nombreRol", r.getNombreRol());
        map.put("descripcion", r.getDescripcion());
        map.put("activo", r.getActivo());
        map.put("permisos", r.getPermisos().stream().map(p -> Map.of(
                "idPermiso", p.getIdPermiso(),
                "nombrePermiso", p.getNombrePermiso(),
                "modulo", p.getModulo() != null ? p.getModulo() : "",
                "accion", p.getAccion() != null ? p.getAccion() : ""
        )).collect(Collectors.toList()));
        return map;
    }
}
