package com.hortifruti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/cadastro", "/cadastro/**",
                                "/recuperar-senha", "/recuperar-senha/**", "/fila/painel",
                                "/adminlte/**", "/error", "/erro/**").permitAll()
                        .requestMatchers("/meu-cadastro").hasRole("FAMILIA")
                        .requestMatchers("/familias/**", "/fila/reiniciar", "/atendentes/**").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("ADMIN", "ATENDENTE"))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            request.getRequestDispatcher("/erro/403").forward(request, response);
                        }))
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isFamilia = authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_FAMILIA".equals(a.getAuthority()));
            if (isFamilia) {
                response.sendRedirect("/meu-cadastro");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    PasswordEncoder staffPasswordEncoder() {
        return new StaffPasswordEncoder();
    }

    @Bean
    PasswordEncoder familiaPasswordEncoder() {
        return new SenhaAcessoPasswordEncoder();
    }

    @Bean
    @Primary
    PasswordEncoder passwordEncoder(@Qualifier("staffPasswordEncoder") PasswordEncoder staff,
                                    @Qualifier("familiaPasswordEncoder") PasswordEncoder familia) {
        PasswordEncoder delegating = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return staff.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword != null && encodedPassword.startsWith("{")) {
                    return delegating.matches(rawPassword, encodedPassword);
                }
                return staff.matches(rawPassword, encodedPassword)
                        || familia.matches(rawPassword, encodedPassword);
            }
        };
    }
}
