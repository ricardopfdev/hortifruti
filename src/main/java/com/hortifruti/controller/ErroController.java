package com.hortifruti.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erro")
public class ErroController {

    @GetMapping("/403")
    public String acessoNegado(Model model, HttpServletRequest request) {
        preencher(model, request, 403, "Acesso negado",
                "Você não tem permissão para acessar este recurso.");
        return "error/erro";
    }

    private void preencher(Model model, HttpServletRequest request, int codigo,
                           String titulo, String mensagem) {
        model.addAttribute("codigo", codigo);
        model.addAttribute("titulo", titulo);
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("caminho", request.getRequestURI());
    }
}
