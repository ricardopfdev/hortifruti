package com.hortifruti.service;

import com.hortifruti.entity.Atendente;
import com.hortifruti.repository.AtendenteRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtendenteService {

    private final AtendenteRepository atendenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;

    public AtendenteService(AtendenteRepository atendenteRepository,
                            @Qualifier("staffPasswordEncoder") PasswordEncoder passwordEncoder,
                            @Value("${hortifruti.security.admin.username}") String adminUsername) {
        this.atendenteRepository = atendenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
    }

    public List<Atendente> listarTodos() {
        return atendenteRepository.findAllByOrderByNomeCompletoAsc();
    }

    public Atendente buscarPorId(Long id) {
        return atendenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atendente não encontrado"));
    }

    @Transactional
    public Atendente cadastrar(Atendente atendente) {
        atendente.setUsername(normalizarUsername(atendente.getUsername()));
        validarUsername(atendente);
        String senha = SenhaUtil.gerarSenhaStaff();
        aplicarSenha(atendente, senha);
        return atendenteRepository.save(atendente);
    }

    @Transactional
    public Atendente atualizar(Atendente atendente) {
        Atendente existente = buscarPorId(atendente.getId());
        existente.setNomeCompleto(atendente.getNomeCompleto());
        existente.setUsername(normalizarUsername(atendente.getUsername()));
        existente.setAtivo(atendente.getAtivo() != null ? atendente.getAtivo() : true);
        validarUsername(existente);
        return atendenteRepository.save(existente);
    }

    @Transactional
    public String redefinirSenha(Long id) {
        Atendente atendente = buscarPorId(id);
        String novaSenha = SenhaUtil.gerarSenhaStaff();
        aplicarSenha(atendente, novaSenha);
        atendenteRepository.save(atendente);
        return novaSenha;
    }

    @Transactional
    public void definirSenha(Long id, String novaSenha) {
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }
        if (novaSenha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        Atendente atendente = buscarPorId(id);
        aplicarSenha(atendente, novaSenha.trim());
        atendenteRepository.save(atendente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!atendenteRepository.existsById(id)) {
            throw new IllegalArgumentException("Atendente não encontrado");
        }
        atendenteRepository.deleteById(id);
    }

    @Transactional
    public Atendente criarSeNaoExistir(String username, String nomeCompleto, String senha) {
        return atendenteRepository.findByUsername(username)
                .orElseGet(() -> {
                    Atendente atendente = new Atendente();
                    atendente.setUsername(normalizarUsername(username));
                    atendente.setNomeCompleto(nomeCompleto);
                    atendente.setAtivo(true);
                    aplicarSenha(atendente, senha);
                    return atendenteRepository.save(atendente);
                });
    }

    private void aplicarSenha(Atendente atendente, String senha) {
        atendente.setSenhaAcesso(senha);
        atendente.setPasswordHash(passwordEncoder.encode(senha));
    }

    private void validarUsername(Atendente atendente) {
        String username = normalizarUsername(atendente.getUsername());
        if (username.equalsIgnoreCase(adminUsername)) {
            throw new IllegalArgumentException("Este usuário é reservado para o administrador.");
        }
        Long id = atendente.getId() != null ? atendente.getId() : -1L;
        if (atendenteRepository.existsByUsernameAndIdNot(username, id)) {
            throw new IllegalArgumentException("Usuário de login já cadastrado.");
        }
        atendente.setUsername(username);
    }

    private String normalizarUsername(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }
}
