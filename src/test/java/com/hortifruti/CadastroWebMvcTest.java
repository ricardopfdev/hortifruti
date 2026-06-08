package com.hortifruti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CadastroWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fluxoCadastroAteSucesso() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/cadastro").session(session))
                .andExpect(status().isOk());

        mockMvc.perform(post("/cadastro")
                        .session(session)
                        .with(csrf())
                        .param("nomeCompleto", "Família Teste")
                        .param("cpf", "529.982.247-25")
                        .param("telefone", "11999999999")
                        .param("quantidadeMoradores", "3")
                        .param("endereco", "Rua Teste")
                        .param("numero", "100")
                        .param("bairro", "Centro")
                        .param("cidade", "São Paulo"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/revisar"));

        mockMvc.perform(post("/cadastro/finalizar")
                        .session(session)
                        .with(csrf())
                        .param("nomeCompleto", "Família Teste")
                        .param("cpf", "52998224725")
                        .param("telefone", "11999999999")
                        .param("quantidadeMoradores", "3")
                        .param("endereco", "Rua Teste")
                        .param("numero", "100")
                        .param("bairro", "Centro")
                        .param("cidade", "São Paulo")
                        .param("senhaGerada", "A1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
