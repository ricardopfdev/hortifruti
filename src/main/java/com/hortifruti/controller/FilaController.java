package com.hortifruti.controller;

import com.hortifruti.service.EntregaService;
import com.hortifruti.service.FamiliaService;
import com.hortifruti.service.FilaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fila")
public class FilaController {

    private final FilaService filaService;
    private final FamiliaService familiaService;
    private final EntregaService entregaService;

    public FilaController(FilaService filaService,
                          FamiliaService familiaService,
                          EntregaService entregaService) {
        this.filaService = filaService;
        this.familiaService = familiaService;
        this.entregaService = entregaService;
    }

    @GetMapping({"", "/", "/lista"})
    public String lista(Model model) {
        model.addAttribute("familias", filaService.listarNaFila());
        model.addAttribute("totalFila", familiaService.familiasNaFila());
        model.addAttribute("proximaSenha", filaService.proximoNumeroSenha());
        return "fila/lista";
    }

    @PostMapping("/gerar-senha/{id}")
    public String gerarSenha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var familia = filaService.gerarSenha(id);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Senha " + String.format("%03d", familia.getNumeroSenha()) + " gerada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/fila";
    }

    @PostMapping("/atender/{id}")
    public String atender(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            entregaService.registrarEntrega(familiaService.buscarPorId(id));
            redirectAttributes.addFlashAttribute("sucesso", "Entrega registrada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/fila";
    }

    @GetMapping("/painel")
    public String painel(Model model) {
        model.addAttribute("familias", filaService.painelSenhas());
        return "fila/painel";
    }

    @GetMapping("/baixa")
    public String baixa() {
        return "fila/baixa";
    }

    @PostMapping("/baixa")
    public String confirmarBaixa(@RequestParam("senha") Integer senha,
                                 RedirectAttributes redirectAttributes) {
        try {
            var entrega = entregaService.registrarPorSenha(senha);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Entrega da senha " + String.format("%03d", entrega.getSenha()) + " confirmada.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/fila/baixa";
    }
}
