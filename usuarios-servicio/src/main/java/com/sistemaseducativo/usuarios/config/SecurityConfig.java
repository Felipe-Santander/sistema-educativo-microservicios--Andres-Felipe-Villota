package com.sistemaseducativo.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// Importa RequestMatcher si usas rutas específicas con antMatchers o mvcMatchers
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // Indica que esta clase contiene configuración de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring Security
public class SecurityConfig {

    // --- Bean para el Codificador de Contraseñas ---
    @Bean // Expone este método como un Bean de Spring
    public PasswordEncoder passwordEncoder() {
        // BCrypt es el estándar recomendado para hashing de contraseñas
        return new BCryptPasswordEncoder();
    }

    // --- Bean para el AuthenticationManager ---
    // Necesario para procesar la autenticación (ej. en el endpoint de login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // --- Bean para configurar la Cadena de Filtros de Seguridad ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF (Cross-Site Request Forgery)
            // No es necesario para APIs REST stateless que usan tokens
            .csrf(csrf -> csrf.disable())

            // 2. Configurar la gestión de sesiones como STATELESS
            // No crearemos sesiones HTTP, cada petición se autenticará por sí misma (con JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 3. Configurar reglas de autorización para las peticiones HTTP
            .authorizeHttpRequests(auth -> auth
                // Permite el acceso público a los endpoints de login y registro (los crearemos después)
                .requestMatchers("/api/usuarios/login", "/api/usuarios/register").permitAll()
                // Para cualquier otra petición bajo /api/usuarios/, requiere autenticación
                .requestMatchers("/api/usuarios/**").authenticated()
                // (Opcional) Podrías añadir reglas más específicas aquí si tuvieras roles (ej. .requestMatchers("/admin/**").hasRole("ADMIN"))
                // Cualquier otra petición no definida explícitamente también requerirá autenticación
                .anyRequest().authenticated()
            );
             // Nota: Más adelante añadiremos aquí los filtros JWT

        // Construye y devuelve la cadena de filtros configurada
        return http.build();
    }
}