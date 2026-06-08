package com.hortifruti.controller;

import com.hortifruti.service.UsuarioService;
import com.hortifruti.validation.CpfUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeuCadastroController {

    private final UsuarioService usuarioService;

    public MeuCadastroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/meu-cadastro")
    public String meuCadastro(Authentication authentication, Model model) {
        var usuario = usuarioService.buscarPorUsername(authentication.getName());
        model.addAttribute("familia", usuario.getFamilia());
        model.addAttribute("usuario", usuario);
        model.addAttribute("cpfFormatado", CpfUtil.formatar(usuario.getUsername()));
        return "cadastro/meu-cadastro";
    }
}
