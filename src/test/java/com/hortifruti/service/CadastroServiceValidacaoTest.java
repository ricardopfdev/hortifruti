package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CadastroServiceValidacaoTest {

    @Autowired
    private CadastroService cadastroService;

    @Test
    void validarPreCadastroAceitaDadosValidos() {
        assertThatCode(() -> cadastroService.validarPreCadastro(novaFamilia("52998224725")))
                .doesNotThrowAnyException();
    }

    @Test
    void validarPreCadastroRejeitaCpfInvalido() {
        assertThatThrownBy(() -> cadastroService.validarPreCadastro(novaFamilia("11111111111")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF inválido");
    }

    private Familia novaFamilia(String cpf) {
        Familia familia = new Familia();
        familia.setNomeCompleto("Família Teste");
        familia.setCpf(cpf);
        familia.setTelefone("11999999999");
        familia.setEndereco("Rua Teste");
        familia.setNumero("100");
        familia.setBairro("Centro");
        familia.setCidade("São Paulo");
        familia.setQuantidadeMoradores(3);
        return familia;
    }
}
