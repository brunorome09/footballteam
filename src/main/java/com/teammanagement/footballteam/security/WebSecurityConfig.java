package com.teammanagement.footballteam.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
public class WebSecurityConfig {

    @Bean
    public BasicAuthenticationEntryPoint cbasicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("footballteam");
        return entryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = cbasicAuthenticationEntryPoint();
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF usando lambdas
                .authorizeHttpRequests(auth -> auth // Configura autorizaciones
                        .anyRequest().authenticated() // Requiere autenticación para cualquier solicitud
                )
                .httpBasic(basic -> basic // Configura autenticación básica
                        .authenticationEntryPoint(basicAuthenticationEntryPoint)
                ) // Configura autenticación básica
                .sessionManagement(session -> session // Configura gestión de sesiones
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("test").password(passwordEncoder().encode("12345")).roles().build());

        return manager;
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
                .userDetailsService(userDetailsService()) // Configura el servicio de usuarios
                .passwordEncoder(passwordEncoder()); // Configura el codificador de contraseñas

        return authenticationManagerBuilder.build(); // Ahora puedes construir el AuthenticationManager
    }
}
