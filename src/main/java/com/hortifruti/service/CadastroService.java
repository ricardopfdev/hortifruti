package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.model.CadastroResult;
import com.hortifruti.repository.FamiliaRepository;
import com.hortifruti.repository.UsuarioRepository;
import com.hortifruti.validation.CpfUtil;
import com.hortifruti.validation.SenhaAcessoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroService {

    private final FamiliaService familiaService;
    private final UsuarioService usuarioService;
    private final FilaService filaService;
    private final UsuarioRepository usuarioRepository;
    private final FamiliaRepository familiaRepository;

    public CadastroService(FamiliaService familiaService,
                           UsuarioService usuarioService,
                           FilaService filaService,
                           UsuarioRepository usuarioRepository,
                           FamiliaRepository familiaRepository) {
        this.familiaService = familiaService;
        this.usuarioService = usuarioService;
        this.filaService = filaService;
        this.usuarioRepository = usuarioRepository;
        this.familiaRepository = familiaRepository;
    }

    public void validarPreCadastro(Familia familia) {
        if (familia.getQuantidadeMoradores() == null || familia.getQuantidadeMoradores() < 1) {
            throw new IllegalArgumentException("A família deve ter pelo menos 1 morador");
        }

        String cpf = CpfUtil.normalizar(familia.getCpf());
        if (!CpfUtil.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (usuarioRepository.existsByUsername(cpf) || familiaRepository.existsByCpfAndIdNot(cpf, -1L)) {
            throw new IllegalArgumentException(
                    "Este CPF já está cadastrado. Faça login ou recupere sua senha.");
        }

        familia.setCpf(cpf);
    }

    @Transactional
    public CadastroResult cadastrar(Familia familia, String senhaPreGerada) {
        validarPreCadastro(familia);

        if (senhaPreGerada == null || senhaPreGerada.isBlank()) {
            throw new IllegalArgumentException("Senha de acesso não informada. Refaça o cadastro.");
        }
        senhaPreGerada = SenhaAcessoUtil.normalizarEntrada(senhaPreGerada);
        if (!SenhaAcessoUtil.isSenhaFamilia(senhaPreGerada)) {
            throw new IllegalArgumentException("Senha de acesso inválida. Refaça o cadastro.");
        }

        familia.setAtiva(true);
        Familia salva = familiaService.salvar(familia);
        usuarioService.criarAcessoFamilia(salva, senhaPreGerada);
        salva = usuarioService.gerarSenhaRetiradaSeNecessario(salva, filaService);

        return new CadastroResult(salva, CpfUtil.normalizar(salva.getCpf()), senhaPreGerada, salva.getNumeroSenha());
    }
}
