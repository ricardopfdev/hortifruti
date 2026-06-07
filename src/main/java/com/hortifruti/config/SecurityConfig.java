package com.hortifruti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/fila/painel", "/adminlte/**", "/error", "/erro/**").permitAll()
                        .requestMatchers("/familias/**", "/fila/reiniciar").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("ADMIN", "ATENDENTE"))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            request.getRequestDispatcher("/erro/403").forward(request, response);
                        }))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(
            @Value("${hortifruti.security.admin.username}") String adminUsername,
            @Value("${hortifruti.security.admin.password}") String adminPassword,
            @Value("${hortifruti.security.atendente.username}") String atendenteUsername,
            @Value("${hortifruti.security.atendente.password}") String atendentePassword,
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        UserDetails atendente = User.builder()
                .username(atendenteUsername)
                .password(passwordEncoder.encode(atendentePassword))
                .roles("ATENDENTE")
                .build();

        return new InMemoryUserDetailsManager(admin, atendente);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
