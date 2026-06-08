package com.hortifruti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RecuperarSenhaWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.hortifruti.service.CadastroService cadastroService;

    @Test
    void recuperarSenhaRedirecionaParaLogin() throws Exception {
        var familia = new com.hortifruti.entity.Familia();
        familia.setNomeCompleto("Família Teste");
        familia.setCpf("52998224725");
        familia.setTelefone("11999999999");
        familia.setEndereco("Rua Teste");
        familia.setNumero("100");
        familia.setBairro("Centro");
        familia.setCidade("São Paulo");
        familia.setQuantidadeMoradores(3);
        cadastroService.cadastrar(familia, "A1234");

        mockMvc.perform(post("/recuperar-senha")
                        .with(csrf())
                        .param("cpf", "529.982.247-25")
                        .param("telefone", "11999999999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
