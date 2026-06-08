package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CadastroServiceTest {

    @Autowired
    private CadastroService cadastroService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void cadastrarCriaFamiliaUsuarioESenha() {
        Familia familia = novaFamilia("52998224725");

        var resultado = cadastroService.cadastrar(familia, "A1234");

        assertThat(resultado.senhaGerada()).isEqualTo("A1234");
        assertThat(resultado.username()).isEqualTo("52998224725");
        assertThat(resultado.senhaRetirada()).isNotNull();
        assertThat(usuarioRepository.findByFamiliaId(resultado.familia().getId())).isPresent();
    }

    @Test
    void cadastrarComCpfDuplicadoFalha() {
        cadastroService.cadastrar(novaFamilia("52998224725"), "A1234");

        assertThatThrownBy(() -> cadastroService.cadastrar(novaFamilia("529.982.247-25"), "B5678"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF já está cadastrado");
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
