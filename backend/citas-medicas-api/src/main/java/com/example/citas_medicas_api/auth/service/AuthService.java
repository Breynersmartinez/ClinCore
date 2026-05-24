package com.example.citas_medicas_api.auth.service;

import com.example.citas_medicas_api.auth.DTO.AuthResponse;
import com.example.citas_medicas_api.auth.DTO.LoginRequest;
import com.example.citas_medicas_api.jwt.utils.JwtUtil;
import com.example.citas_medicas_api.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Autentica con Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Genera tokens
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Actualiza último acceso
        usuarioRepository.findByUsername(request.getUsername())
                .ifPresent(u -> {
                    u.setUltimoAcceso(LocalDate.now());
                    usuarioRepository.save(u);
                });

        // Separa roles y permisos
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .collect(Collectors.toList());

        List<String> permisos = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toList());

        log.info("Login exitoso: {} | Roles: {}", request.getUsername(), roles);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(userDetails.getUsername())
                .roles(roles)
                .permisos(permisos)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new com.example.citas_medicas_api.exception.BusinessException("Refresh token inválido o expirado");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(username)
                .build();
    }
}