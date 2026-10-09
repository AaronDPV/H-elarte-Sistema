package com.example.helarte.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos: registro y catálogo de servicios
                        .requestMatchers("/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicios", "/servicios/**").permitAll()

                        // Login procesado mediante la pestaña Authorization (Basic Auth)
                        .requestMatchers("/auth/login").authenticated()

                        // Endpoints exclusivos de ADMINISTRADOR
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/servicios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicios/**").hasRole("ADMIN")

                        // Endpoints para ADMINISTRADOR y EMPLEADO/COLABORADOR
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.POST, "/mesas/**").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.PUT, "/mesas/**").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.DELETE, "/mesas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservas").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET, "/reservas/fecha").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET, "/reservas/estado").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")
                        .requestMatchers(HttpMethod.PUT, "/reservas/*/estado").hasAnyRole("ADMIN", "EMPLEADO", "COLABORADOR")

                        // Endpoints para CLIENTE autenticado (y roles superiores)
                        .requestMatchers("/reservas/mis-reservas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/reservas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/reservas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/reservas/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/mesas/**").authenticated()

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}