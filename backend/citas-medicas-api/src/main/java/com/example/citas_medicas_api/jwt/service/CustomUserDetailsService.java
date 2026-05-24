package com.example.citas_medicas_api.jwt.service;

import com.example.citas_medicas_api.permission.model.Permiso;
import com.example.citas_medicas_api.role.model.Rol;
import com.example.citas_medicas_api.user.model.Usuario;
import com.example.citas_medicas_api.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        if (!"S".equals(usuario.getActivo())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPasswordHash(),
                buildAuthorities(usuario)
        );
    }

    /**
     * Construye las autoridades del usuario combinando:
     * 1. Roles: ROLE_MEDICO, ROLE_ADMINISTRATIVO, etc.
     * 2. Permisos específicos: CONSULTORIOS_CREATE, CITAS_READ, etc.
     */
    private Collection<? extends GrantedAuthority> buildAuthorities(Usuario usuario) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Rol rol : usuario.getRoles()) {
            // Agrega el rol como authority: ROLE_MEDICO
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()));

            // Agrega permisos del rol como authorities: CONSULTORIOS_READ
            for (Permiso permiso : rol.getPermisos()) {
                if ("S".equals(permiso.getActivo())) {
                    authorities.add(new SimpleGrantedAuthority(permiso.getNombrePermiso()));
                }
            }
        }

        return authorities;
    }
}
