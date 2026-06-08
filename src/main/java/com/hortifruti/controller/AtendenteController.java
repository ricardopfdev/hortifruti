package com.hortifruti.controller;

import com.hortifruti.entity.Atendente;
import com.hortifruti.service.AtendenteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/atendentes")
public class AtendenteController {

    private final AtendenteService atendenteService;

    public AtendenteController(AtendenteService atendenteService) {
        this.atendenteService = atendenteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("atendentes", atendenteService.listarTodos());
        return "atendentes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Atendente atendente = new Atendente();
        atendente.setAtivo(true);
        model.addAttribute("atendente", atendente);
        model.addAttribute("novo", true);
        return "atendentes/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Atendente atendente,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("novo", atendente.getId() == null);
            return "atendentes/form";
        }

        try {
            if (atendente.getId() == null) {
                Atendente salvo = atendenteService.cadastrar(atendente);
                redirectAttributes.addFlashAttribute("senhaGerada", salvo.getSenhaAcesso());
                redirectAttributes.addFlashAttribute("usernameGerado", salvo.getUsername());
                redirectAttributes.addFlashAttribute("sucesso",
                        "Atendente cadastrado. Informe o usuário e a senha gerada para o ajudante.");
            } else {
                atendenteService.atualizar(atendente);
                redirectAttributes.addFlashAttribute("sucesso", "Atendente atualizado com sucesso.");
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("novo", atendente.getId() == null);
            return "atendentes/form";
        }

        return "redirect:/atendentes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("atendente", atendenteService.buscarPorId(id));
        model.addAttribute("novo", false);
        return "atendentes/form";
    }

    @PostMapping("/redefinir-senha/{id}")
    public String redefinirSenhaAutomatica(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String novaSenha = atendenteService.redefinirSenha(id);
            Atendente atendente = atendenteService.buscarPorId(id);
            redirectAttributes.addFlashAttribute("senhaGerada", novaSenha);
            redirectAttributes.addFlashAttribute("usernameGerado", atendente.getUsername());
            redirectAttributes.addFlashAttribute("sucesso", "Nova senha gerada para o atendente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/atendentes/editar/" + id;
    }

    @PostMapping("/definir-senha/{id}")
    public String definirSenha(@PathVariable Long id,
                               @RequestParam String novaSenha,
                               RedirectAttributes redirectAttributes) {
        try {
            atendenteService.definirSenha(id, novaSenha);
            redirectAttributes.addFlashAttribute("sucesso", "Senha do atendente alterada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/atendentes/editar/" + id;
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            atendenteService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Atendente excluído com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/atendentes";
    }
}
