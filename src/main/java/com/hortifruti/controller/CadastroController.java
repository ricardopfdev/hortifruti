package com.hortifruti.controller;

import com.hortifruti.entity.Familia;
import com.hortifruti.model.CadastroResult;
import com.hortifruti.service.CadastroService;
import com.hortifruti.service.SenhaUtil;
import com.hortifruti.validation.CpfUtil;
import com.hortifruti.validation.EnderecoUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

    static final String SESSION_FAMILIA = "cadastroFamilia";
    static final String SESSION_SENHA = "cadastroSenhaGerada";

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @GetMapping
    public String formulario(Model model, HttpSession session) {
        limparSessaoCadastro(session);
        if (!model.containsAttribute("familia")) {
            model.addAttribute("familia", new Familia());
        }
        return "cadastro/form";
    }

    @PostMapping
    public String revisar(@Valid @ModelAttribute Familia familia,
                          BindingResult bindingResult,
                          Model model,
                          HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "cadastro/form";
        }

        try {
            cadastroService.validarPreCadastro(familia);
            String senhaGerada = SenhaUtil.gerarSenhaAcesso();
            session.setAttribute(SESSION_FAMILIA, familia);
            session.setAttribute(SESSION_SENHA, senhaGerada);
            prepararPaginaRevisao(model, familia, senhaGerada);
            return "cadastro/revisar";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            return "cadastro/form";
        }
    }

    @GetMapping("/revisar")
    public String paginaRevisao(Model model, HttpSession session) {
        Familia familia = (Familia) session.getAttribute(SESSION_FAMILIA);
        String senhaGerada = (String) session.getAttribute(SESSION_SENHA);
        if (familia == null || senhaGerada == null) {
            return "redirect:/cadastro";
        }
        prepararPaginaRevisao(model, familia, senhaGerada);
        return "cadastro/revisar";
    }

    @PostMapping("/corrigir")
    public String corrigir(@ModelAttribute Familia familia,
                           Model model,
                           HttpSession session) {
        limparSessaoCadastro(session);
        model.addAttribute("familia", familia);
        return "cadastro/form";
    }

    @PostMapping("/finalizar")
    public String finalizar(@Valid @ModelAttribute Familia familia,
                              BindingResult bindingResult,
                              @RequestParam String senhaGerada,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararPaginaRevisao(model, familia, senhaGerada);
            return "cadastro/revisar";
        }

        try {
            CadastroResult resultado = cadastroService.cadastrar(familia, senhaGerada);
            limparSessaoCadastro(session);
            redirectAttributes.addFlashAttribute("sucesso", montarMensagemCadastroConcluido(resultado));
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            session.setAttribute(SESSION_FAMILIA, familia);
            session.setAttribute(SESSION_SENHA, senhaGerada);
            prepararPaginaRevisao(model, familia, senhaGerada);
            model.addAttribute("erro", ex.getMessage());
            return "cadastro/revisar";
        }
    }

    @GetMapping("/sucesso")
    public String sucesso(Model model) {
        if (!model.containsAttribute("familia")) {
            return "redirect:/cadastro";
        }
        return "cadastro/sucesso";
    }

    private void prepararPaginaRevisao(Model model, Familia familia, String senhaGerada) {
        model.addAttribute("familia", familia);
        model.addAttribute("senhaGerada", senhaGerada);
        model.addAttribute("cpfFormatado", CpfUtil.formatar(familia.getCpf()));
        model.addAttribute("enderecoCompleto", EnderecoUtil.formatarCompleto(familia));
    }

    private void limparSessaoCadastro(HttpSession session) {
        session.removeAttribute(SESSION_FAMILIA);
        session.removeAttribute(SESSION_SENHA);
    }

    private String montarMensagemCadastroConcluido(CadastroResult resultado) {
        String cpf = CpfUtil.formatar(resultado.username());
        StringBuilder mensagem = new StringBuilder("Cadastro realizado com sucesso! ");
        mensagem.append("Use o CPF ").append(cpf);
        mensagem.append(" e a senha ").append(resultado.senhaGerada()).append(" para entrar.");
        if (resultado.senhaRetirada() != null) {
            mensagem.append(" Senha de retirada na fila: ")
                    .append(String.format("%03d", resultado.senhaRetirada()))
                    .append('.');
        }
        return mensagem.toString();
    }
}
