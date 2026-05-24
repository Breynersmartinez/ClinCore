package com.example.citas_medicas_api.security;

import com.example.citas_medicas_api.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // Habilita @PreAuthorize a nivel de método
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // ── CATÁLOGOS ──────────────────────────────────────────
                        // Todos los roles autenticados pueden leer catálogos
                        .requestMatchers(HttpMethod.GET, "/departamentos/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.GET, "/municipios/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.GET, "/especialidades/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        // Solo ADMINISTRATIVO puede modificar catálogos
                        .requestMatchers("/departamentos/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers("/municipios/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers("/especialidades/**").hasRole("ADMINISTRATIVO")

                        // ── SEDES ──────────────────────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/sedes/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers("/sedes/**").hasRole("ADMINISTRATIVO")

                        // ── CONSULTORIOS ───────────────────────────────────────
                        // Lectura: todos los roles
                        .requestMatchers(HttpMethod.GET, "/consultorios/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        // Escritura: solo ADMINISTRATIVO (CREATE, UPDATE, DELETE)
                        .requestMatchers(HttpMethod.POST, "/consultorios/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.PUT, "/consultorios/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/consultorios/**").hasRole("ADMINISTRATIVO")

                        // ── PERSONAS / MÉDICOS / PACIENTES ─────────────────────
                        .requestMatchers(HttpMethod.GET, "/personas/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.POST, "/personas/**").hasAnyRole(
                                "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/personas/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/personas/**").hasRole("ADMINISTRATIVO")

                        .requestMatchers(HttpMethod.GET, "/medicos/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers("/medicos/**").hasRole("ADMINISTRATIVO")

                        .requestMatchers(HttpMethod.GET, "/pacientes/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.POST, "/pacientes/**").hasAnyRole(
                                "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/pacientes/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**").hasRole("ADMINISTRATIVO")

                        // ── CITAS MÉDICAS ──────────────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/citas/**").hasAnyRole(
                                "MEDICO", "PACIENTE", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.POST, "/citas/**").hasAnyRole(
                                "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/citas/**").hasAnyRole(
                                "MEDICO", "ADMINISTRATIVO", "AUXILIAR_MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/citas/**").hasRole("ADMINISTRATIVO")

                        // ── GESTIÓN DE USUARIOS Y ROLES ────────────────────────
                        .requestMatchers("/usuarios/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers("/roles/**").hasRole("ADMINISTRATIVO")
                        .requestMatchers("/permisos/**").hasRole("ADMINISTRATIVO")

                        // ── AUDITORÍA ──────────────────────────────────────────
                        .requestMatchers("/auditoria/**").hasRole("ADMINISTRATIVO")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split("http://localhost:5173/, http://localhost:5173")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

