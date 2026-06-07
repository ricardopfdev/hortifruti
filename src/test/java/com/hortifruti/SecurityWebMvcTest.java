package com.hortifruti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void painelDeSenhasEhPublico() throws Exception {
        mockMvc.perform(get("/fila/painel"))
                .andExpect(status().isOk());
    }

    @Test
    void loginPageEhPublica() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void dashboardRequerAutenticacao() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dashboardAcessivelParaAdmin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ATENDENTE")
    void familiasBloqueadoParaAtendente() throws Exception {
        mockMvc.perform(get("/familias"))
                .andExpect(status().isForbidden());
    }
}
