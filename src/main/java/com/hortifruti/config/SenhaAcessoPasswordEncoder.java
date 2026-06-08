package com.hortifruti.config;

import com.hortifruti.validation.SenhaAcessoUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Senhas de famílias — 5 caracteres (1 letra + 4 números), normalizadas para maiúsculas no login.
 */
public class SenhaAcessoPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(SenhaAcessoUtil.normalizarEntrada(rawPassword.toString()));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return delegate.matches(SenhaAcessoUtil.normalizarEntrada(rawPassword.toString()), encodedPassword);
    }
}
