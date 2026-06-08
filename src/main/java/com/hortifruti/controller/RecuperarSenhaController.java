package com.hortifruti.controller;

import com.hortifruti.model.RecuperacaoSenhaResult;
import com.hortifruti.service.RecuperarSenhaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recuperar-senha")
public class RecuperarSenhaController {

    private final RecuperarSenhaService recuperarSenhaService;

    public RecuperarSenhaController(RecuperarSenhaService recuperarSenhaService) {
        this.recuperarSenhaService = recuperarSenhaService;
    }

    @GetMapping
    public String formulario() {
        return "recuperar-senha/form";
    }

    @PostMapping
    public String recuperar(@RequestParam String cpf,
                            @RequestParam String telefone,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            var resultado = recuperarSenhaService.recuperar(cpf, telefone);
            redirectAttributes.addFlashAttribute("sucesso", montarMensagemSucesso(resultado));
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("cpf", cpf);
            model.addAttribute("telefone", telefone);
            return "recuperar-senha/form";
        }
    }

    @GetMapping("/sucesso")
    public String sucesso() {
        return "redirect:/recuperar-senha";
    }

    private String montarMensagemSucesso(RecuperacaoSenhaResult resultado) {
        StringBuilder mensagem = new StringBuilder("Senha recuperada com sucesso! ");
        mensagem.append("Use o CPF ").append(resultado.cpfFormatado());
        mensagem.append(" e a nova senha ").append(resultado.senhaAcesso()).append(" para entrar.");
        if (resultado.senhaRetirada() != null) {
            mensagem.append(" Senha de retirada na fila: ")
                    .append(String.format("%03d", resultado.senhaRetirada()))
                    .append('.');
        }
        return mensagem.toString();
    }
}
