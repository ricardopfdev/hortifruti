package com.hortifruti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ErrorPageWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void paginaInexistenteRetorna404Amigavel() throws Exception {
        mockMvc.perform(get("/rota-inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/erro"))
                .andExpect(model().attribute("codigo", 404));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void familiaInexistenteRetornaErroAmigavel() throws Exception {
        mockMvc.perform(get("/familias/editar/999999"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/erro"))
                .andExpect(model().attribute("titulo", "Operação não permitida"))
                .andExpect(model().attribute("mensagem", "Família não encontrada"));
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void acessoNegadoRetorna403Amigavel() throws Exception {
        mockMvc.perform(get("/familias"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/erro/403"));
    }
}
