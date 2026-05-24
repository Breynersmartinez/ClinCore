package com.example.citas_medicas_api.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(auth.getName());
        };
    }

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("API Gestión de Citas Médicas")
                        .version("1.0.0")
                        .description("""
                    Sistema REST para gestión de citas médicas con control de acceso por roles.
                    
                    **Roles disponibles:**
                    - `MEDICO` → Lectura general + actualización de citas
                    - `PACIENTE` → Solo sus propios datos
                    - `ADMINISTRATIVO` → Acceso completo
                    - `AUXILIAR_MEDICO` → Lectura + registro de personas y citas
                    """)
                        .contact(new Contact()
                                .name("Electiva I - Base de Datos")
                                .email("docente@universidad.edu.co")));
    }
}

