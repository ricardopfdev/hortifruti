package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.entity.Usuario;
import com.hortifruti.model.RecuperacaoSenhaResult;
import com.hortifruti.repository.FamiliaRepository;
import com.hortifruti.repository.UsuarioRepository;
import com.hortifruti.validation.CpfUtil;
import com.hortifruti.validation.EnderecoUtil;
import com.hortifruti.validation.SenhaAcessoUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecuperarSenhaService {

    private final FamiliaRepository familiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RecuperarSenhaService(FamiliaRepository familiaRepository,
                                 UsuarioRepository usuarioRepository,
                                 @Qualifier("familiaPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.familiaRepository = familiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RecuperacaoSenhaResult recuperar(String cpf, String telefone) {
        String cpfNormalizado = CpfUtil.normalizar(cpf);
        if (!CpfUtil.isValid(cpfNormalizado)) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        Familia familia = familiaRepository.findByCpf(cpfNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado. Verifique os dados ou faça um novo cadastro."));

        if (!EnderecoUtil.normalizarTelefone(familia.getTelefone())
                .equals(EnderecoUtil.normalizarTelefone(telefone))) {
            throw new IllegalArgumentException("CPF e telefone não conferem.");
        }

        Usuario usuario = usuarioRepository.findByUsernameAndAtivoTrue(cpfNormalizado)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cadastro encontrado, mas sem acesso ao sistema. Procure o administrador."));

        String novaSenha = SenhaUtil.gerarSenhaAcesso();
        usuario.setSenhaAcesso(novaSenha);
        usuario.setPasswordHash(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        return new RecuperacaoSenhaResult(
                CpfUtil.formatar(cpfNormalizado),
                novaSenha,
                familia.getNumeroSenha());
    }
}
