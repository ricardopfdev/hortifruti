package com.hortifruti.service;

import com.hortifruti.repository.AtendenteRepository;
import com.hortifruti.repository.UsuarioRepository;
import com.hortifruti.validation.CpfUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private static final String NOOP_PREFIX = "{noop}";

    private final UsuarioRepository usuarioRepository;
    private final AtendenteRepository atendenteRepository;
    private final String adminUsername;
    private final String adminPassword;

    public AppUserDetailsService(UsuarioRepository usuarioRepository,
                                 AtendenteRepository atendenteRepository,
                                 @Value("${hortifruti.security.admin.username}") String adminUsername,
                                 @Value("${hortifruti.security.admin.password}") String adminPassword) {
        this.usuarioRepository = usuarioRepository;
        this.atendenteRepository = atendenteRepository;
        this.adminUsername = adminUsername == null ? "" : adminUsername.trim();
        this.adminPassword = adminPassword == null ? "" : adminPassword.trim();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String login = username == null ? "" : username.trim();

        if (adminUsername.equalsIgnoreCase(login)) {
            return User.builder()
                    .username(adminUsername)
                    .password(NOOP_PREFIX + adminPassword)
                    .roles("ADMIN")
                    .build();
        }

        String loginAtendente = login.toLowerCase();
        var atendente = atendenteRepository.findByUsernameAndAtivoTrue(loginAtendente);
        if (atendente.isPresent()) {
            return staffUser(
                    atendente.get().getUsername(),
                    atendente.get().getPasswordHash(),
                    "ATENDENTE");
        }

        String cpf = CpfUtil.normalizar(login);
        return usuarioRepository.findByUsernameAndAtivoTrue(cpf)
                .map(usuario -> User.builder()
                        .username(usuario.getUsername())
                        .password(usuario.getPasswordHash())
                        .roles(usuario.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private UserDetails staffUser(String username, String encodedPassword, String role) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .roles(role)
                .build();
    }
}
