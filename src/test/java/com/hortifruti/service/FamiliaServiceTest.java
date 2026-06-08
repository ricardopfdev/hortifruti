package com.hortifruti.service;

import com.hortifruti.entity.Familia;
import com.hortifruti.model.StatusFila;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FamiliaServiceTest {

    @Autowired
    private FamiliaService familiaService;

    @Test
    void salvarFamiliaNovaEntraNaFila() {
        Familia familia = familiaValida("52998224725");

        Familia salva = familiaService.salvar(familia);

        assertNotNull(salva.getId());
        assertEquals(StatusFila.NA_FILA, salva.getStatusFila());
        assertTrue(salva.getPrioridade() > 0);
        assertTrue(salva.getAtiva());
        assertEquals("52998224725", salva.getCpf());
    }

    @Test
    void rejeitaCpfDuplicado() {
        familiaService.salvar(familiaValida("39053344705"));

        assertThrows(IllegalArgumentException.class,
                () -> familiaService.salvar(familiaValida("39053344705")));
    }

    @Test
    void rejeitaFamiliaSemMoradores() {
        Familia familia = familiaValida("11144477735");
        familia.setQuantidadeMoradores(0);

        assertThrows(IllegalArgumentException.class, () -> familiaService.salvar(familia));
    }

    @Test
    void rejeitaCpfInvalido() {
        Familia familia = familiaValida("11111111111");

        assertThrows(IllegalArgumentException.class, () -> familiaService.salvar(familia));
    }

    @Test
    void normalizaCpfComPontuacao() {
        Familia familia = familiaValida("529.982.247-25");

        Familia salva = familiaService.salvar(familia);

        assertEquals("52998224725", salva.getCpf());
    }

    private Familia familiaValida(String cpf) {
        Familia familia = new Familia();
        familia.setNomeCompleto("Família Teste");
        familia.setCpf(cpf);
        familia.setTelefone("11999999999");
        familia.setEndereco("Rua Teste");
        familia.setNumero("100");
        familia.setBairro("Centro");
        familia.setCidade("São Paulo");
        familia.setQuantidadeMoradores(3);
        familia.setAtiva(true);
        return familia;
    }
}
