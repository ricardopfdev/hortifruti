package com.hortifruti.service;

import com.hortifruti.entity.Entrega;
import com.hortifruti.entity.Familia;
import com.hortifruti.model.FiltroEntrega;
import com.hortifruti.model.FiltroFamilia;
import com.hortifruti.model.StatusFila;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ListagemPaginadaServiceTest {

    @Autowired
    private FamiliaService familiaService;

    @Autowired
    private EntregaService entregaService;

    @Autowired
    private FilaService filaService;

    @BeforeEach
    void prepararDados() {
        familiaService.salvar(criarFamilia("Maria Silva", "52998224725"));

        Familia atendida = familiaService.salvar(criarFamilia("João Santos", "39053344705"));
        filaService.gerarSenha(atendida.getId());
        entregaService.registrarEntrega(familiaService.buscarPorId(atendida.getId()));

        Familia inativa = criarFamilia("Ana Costa", "11144477735");
        inativa.setAtiva(false);
        familiaService.salvar(inativa);
    }

    @Test
    void filtraFamiliasPorNome() {
        FiltroFamilia filtro = new FiltroFamilia();
        filtro.setBusca("Maria");

        var pagina = familiaService.listar(filtro);

        assertEquals(1, pagina.getTotalElements());
        assertEquals("Maria Silva", pagina.getContent().getFirst().getNomeCompleto());
    }

    @Test
    void filtraFamiliasPorStatusFila() {
        FiltroFamilia filtro = new FiltroFamilia();
        filtro.setStatusFila(StatusFila.ATENDIDO);

        var pagina = familiaService.listar(filtro);

        assertEquals(1, pagina.getTotalElements());
        assertEquals(StatusFila.ATENDIDO, pagina.getContent().getFirst().getStatusFila());
    }

    @Test
    void paginaFamiliasRespeitaTamanho() {
        FiltroFamilia filtro = new FiltroFamilia();
        filtro.setSize(2);

        var pagina = familiaService.listar(filtro);

        assertEquals(3, pagina.getTotalElements());
        assertEquals(2, pagina.getContent().size());
        assertEquals(2, pagina.getTotalPages());
    }

    @Test
    void filtraEntregasPorSenha() {
        FiltroEntrega filtro = new FiltroEntrega();
        filtro.setBusca("1");

        var pagina = entregaService.listar(filtro);

        assertEquals(1, pagina.getTotalElements());
        Entrega entrega = pagina.getContent().getFirst();
        assertEquals(1, entrega.getSenha());
    }

    private Familia criarFamilia(String nome, String cpf) {
        Familia familia = new Familia();
        familia.setNomeCompleto(nome);
        familia.setCpf(cpf);
        familia.setTelefone("11999999999");
        familia.setEndereco("Rua Teste, 1");
        familia.setQuantidadeMoradores(2);
        familia.setAtiva(true);
        return familia;
    }
}
