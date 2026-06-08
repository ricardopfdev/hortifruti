package com.hortifruti;

import com.hortifruti.entity.Familia;
import com.hortifruti.service.CadastroService;
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
class LoginFamiliaWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CadastroService cadastroService;

    @Test
    void loginFamiliaComCpfFormatadoESenhaCorreta() throws Exception {
        cadastroService.cadastrar(novaFamilia(), "K1234");

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "529.982.247-25")
                        .param("password", "K1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meu-cadastro"));
    }

    @Test
    void loginFamiliaComSenhaMinuscula() throws Exception {
        cadastroService.cadastrar(novaFamilia(), "K1234");

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "52998224725")
                        .param("password", "k1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meu-cadastro"));
    }

    private Familia novaFamilia() {
        Familia familia = new Familia();
        familia.setNomeCompleto("Família Teste");
        familia.setCpf("52998224725");
        familia.setTelefone("11999999999");
        familia.setEndereco("Rua Teste");
        familia.setNumero("100");
        familia.setBairro("Centro");
        familia.setCidade("São Paulo");
        familia.setQuantidadeMoradores(3);
        return familia;
    }
}
