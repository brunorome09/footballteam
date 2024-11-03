package com.teammanagement.footballteam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BasicAuthenticationEntryPoint cbasicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("footballteam");
        return entryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = cbasicAuthenticationEntryPoint();
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF usando lambdas
                .authorizeHttpRequests(auth -> auth // Configura autorizaciones
                        .anyRequest().authenticated() // Requiere autenticación para cualquier solicitud
                )
                .sessionManagement(session -> session // Configura gestión de sesiones
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilter(jwtAuthenticationFilter).addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // Configura el servicio de usuarios
                .passwordEncoder(passwordEncoder()); // Configura el codificador de contraseñas

        return authenticationManagerBuilder.build(); // Ahora puedes construir el AuthenticationManager
    }
}