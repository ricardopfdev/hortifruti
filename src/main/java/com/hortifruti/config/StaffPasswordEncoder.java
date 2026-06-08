package com.hortifruti.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Senhas de administrador e atendentes — BCrypt padrão, sem regra de 5 caracteres.
 */
public class StaffPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(normalizar(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(normalizar(rawPassword), encodedPassword);
    }

    private String normalizar(CharSequence rawPassword) {
        return rawPassword == null ? "" : rawPassword.toString().trim();
    }
}
