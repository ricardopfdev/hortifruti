package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.entity.Usuario;
import com.hortifruti.model.StatusFila;
import com.hortifruti.repository.UsuarioRepository;
import com.hortifruti.validation.CpfUtil;
import com.hortifruti.validation.SenhaAcessoUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          @Qualifier("familiaPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarPorUsername(String username) {
        String normalizado = CpfUtil.normalizar(username);
        return usuarioRepository.findByUsernameAndAtivoTrue(normalizado)
                .or(() -> usuarioRepository.findByUsernameAndAtivoTrue(username))
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public Optional<Usuario> buscarPorFamiliaId(Long familiaId) {
        return usuarioRepository.findByFamiliaId(familiaId);
    }

    @Transactional
    public String criarAcessoFamilia(Familia familia) {
        return criarAcessoFamilia(familia, SenhaUtil.gerarSenhaAcesso());
    }

    @Transactional
    public String criarAcessoFamilia(Familia familia, String senhaGerada) {
        String cpf = CpfUtil.normalizar(familia.getCpf());
        if (usuarioRepository.existsByUsername(cpf)) {
            throw new IllegalArgumentException("CPF já possui acesso ao sistema.");
        }

        String senhaNormalizada = SenhaAcessoUtil.normalizarEntrada(senhaGerada);
        if (senhaNormalizada.isBlank()) {
            throw new IllegalArgumentException("Senha de acesso inválida.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(cpf);
        usuario.setPasswordHash(passwordEncoder.encode(senhaNormalizada));
        usuario.setSenhaAcesso(senhaNormalizada);
        usuario.setRole("FAMILIA");
        usuario.setFamilia(familia);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        return senhaNormalizada;
    }

    @Transactional
    public Familia gerarSenhaRetiradaSeNecessario(Familia familia, FilaService filaService) {
        if (familia.getNumeroSenha() == null
                && Boolean.TRUE.equals(familia.getAtiva())
                && StatusFila.NA_FILA.equals(familia.getStatusFila())) {
            return filaService.gerarSenha(familia.getId());
        }
        return familia;
    }
}
