package com.hortifruti.controller;

import com.hortifruti.entity.Familia;
import com.hortifruti.model.FiltroFamilia;
import com.hortifruti.service.EntregaService;
import com.hortifruti.service.FamiliaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaService familiaService;
    private final EntregaService entregaService;

    public FamiliaController(FamiliaService familiaService, EntregaService entregaService) {
        this.familiaService = familiaService;
        this.entregaService = entregaService;
    }

    @GetMapping
    public String listar(FiltroFamilia filtro, Model model) {
        var pagina = familiaService.listar(filtro);
        model.addAttribute("filtro", filtro);
        model.addAttribute("pagina", pagina);
        model.addAttribute("familias", pagina.getContent());
        return "familias/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Familia familia = new Familia();
        familia.setAtiva(true);
        model.addAttribute("familia", familia);
        return "familias/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Familia familia,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "familias/form";
        }

        try {
            familiaService.salvar(familia);
            redirectAttributes.addFlashAttribute("sucesso", "Família salva com sucesso.");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            return "familias/form";
        }

        return "redirect:/familias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("familia", familiaService.buscarPorId(id));
        return "familias/form";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            familiaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Família excluída com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/familias";
    }

    @PostMapping("/entregar/{id}")
    public String entregar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            entregaService.registrarEntrega(familiaService.buscarPorId(id));
            redirectAttributes.addFlashAttribute("sucesso", "Entrega registrada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/familias";
    }
}
