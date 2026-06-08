package com.hortifruti.service;

import com.hortifruti.entity.Atendente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AtendenteServiceTest {

    @Autowired
    private AtendenteService atendenteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void cadastrarGeraSenhaEHash() {
        Atendente atendente = novoAtendente("ajudante.teste");

        Atendente salvo = atendenteService.cadastrar(atendente);

        assertNotNull(salvo.getId());
        assertEquals("ajudante.teste", salvo.getUsername());
        assertNotNull(salvo.getSenhaAcesso());
        assertEquals(8, salvo.getSenhaAcesso().length());
        assertTrue(passwordEncoder.matches(salvo.getSenhaAcesso(), salvo.getPasswordHash()));
    }

    @Test
    void rejeitaUsernameDuplicado() {
        atendenteService.cadastrar(novoAtendente("duplicado"));

        assertThrows(IllegalArgumentException.class,
                () -> atendenteService.cadastrar(novoAtendente("duplicado")));
    }

    @Test
    void rejeitaUsernameReservadoParaAdmin() {
        Atendente atendente = novoAtendente("admin");

        assertThrows(IllegalArgumentException.class, () -> atendenteService.cadastrar(atendente));
    }

    @Test
    void definirSenhaAlteraHash() {
        Atendente salvo = atendenteService.cadastrar(novoAtendente("senha.manual"));
        String senhaAnterior = salvo.getSenhaAcesso();

        atendenteService.definirSenha(salvo.getId(), "novaSenha123");

        Atendente atualizado = atendenteService.buscarPorId(salvo.getId());
        assertEquals("novaSenha123", atualizado.getSenhaAcesso());
        assertNotEquals(senhaAnterior, atualizado.getSenhaAcesso());
        assertTrue(passwordEncoder.matches("novaSenha123", atualizado.getPasswordHash()));
    }

    @Test
    void redefinirSenhaGeraNovaSenha() {
        Atendente salvo = atendenteService.cadastrar(novoAtendente("reset.senha"));
        String senhaAnterior = salvo.getSenhaAcesso();

        String novaSenha = atendenteService.redefinirSenha(salvo.getId());

        assertNotEquals(senhaAnterior, novaSenha);
        Atendente atualizado = atendenteService.buscarPorId(salvo.getId());
        assertEquals(novaSenha, atualizado.getSenhaAcesso());
    }

    @Test
    void rejeitaSenhaCurta() {
        Atendente salvo = atendenteService.cadastrar(novoAtendente("senha.curta"));

        assertThrows(IllegalArgumentException.class,
                () -> atendenteService.definirSenha(salvo.getId(), "12345"));
    }

    private Atendente novoAtendente(String username) {
        Atendente atendente = new Atendente();
        atendente.setNomeCompleto("Ajudante Teste");
        atendente.setUsername(username);
        atendente.setAtivo(true);
        return atendente;
    }
}
