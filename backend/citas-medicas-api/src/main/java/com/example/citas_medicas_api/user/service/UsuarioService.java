package com.example.citas_medicas_api.user.service;

import com.example.citas_medicas_api.exception.BusinessException;
import com.example.citas_medicas_api.exception.ResourceNotFoundException;
import com.example.citas_medicas_api.role.model.Rol;
import com.example.citas_medicas_api.role.repository.RolRepository;
import com.example.citas_medicas_api.user.DTO.UsuarioRequest;
import com.example.citas_medicas_api.user.model.Usuario;
import com.example.citas_medicas_api.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarPorId(Long id) {
        return toMap(findById(id));
    }

    @Transactional
    public Map<String, Object> crear(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("El username '" + request.getUsername() + "' ya está registrado");
        }
        if (request.getEmail() != null && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está registrado");
        }

        Set<Rol> roles = resolverRoles(request.getRoleIds());

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .activo("S")
                .fechaCreacion(LocalDate.now())
                .roles(roles)
                .build();

        return toMap(usuarioRepository.save(usuario));
    }

    @Transactional
    public Map<String, Object> actualizarRoles(Long idUsuario, Set<Long> roleIds) {
        Usuario usuario = findById(idUsuario);
        usuario.setRoles(resolverRoles(roleIds));
        log.info("Roles actualizados para usuario id={}: {}", idUsuario, roleIds);
        return toMap(usuarioRepository.save(usuario));
    }

    @Transactional
    public void cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = findById(id);
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void inactivar(Long id) {
        Usuario usuario = findById(id);
        usuario.setActivo("N");
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void activar(Long id) {
        Usuario usuario = findById(id);
        usuario.setActivo("S");
        usuarioRepository.save(usuario);
    }

    private Set<Rol> resolverRoles(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) return new HashSet<>();
        return roleIds.stream()
                .map(rid -> rolRepository.findById(rid)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol", rid)))
                .collect(Collectors.toSet());
    }

    private Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private Map<String, Object> toMap(Usuario u) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("idUsuario", u.getIdUsuario());
        map.put("username", u.getUsername());
        map.put("email", u.getEmail());
        map.put("activo", u.getActivo());
        map.put("fechaCreacion", u.getFechaCreacion());
        map.put("ultimoAcceso", u.getUltimoAcceso());
        map.put("roles", u.getRoles().stream()
                .map(r -> Map.of("idRol", r.getIdRol(), "nombreRol", r.getNombreRol()))
                .collect(Collectors.toList()));
        return map;
    }
}
