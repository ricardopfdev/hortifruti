package com.hortifruti.controller;

import com.hortifruti.entity.Familia;
import com.hortifruti.service.FamiliaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaService familiaService;

    public FamiliaController(FamiliaService familiaService) {
        this.familiaService = familiaService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("familias", familiaService.listarTodas());
        return "familias/lista";
    }

    // FORM NOVA FAMÍLIA
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("familia", new Familia());
        return "familias/form";
    }

    // SALVAR
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Familia familia) {
        familiaService.salvar(familia);
        return "redirect:/familias";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("familia", familiaService.buscarPorId(id));
        return "familias/form";
    }

    // DELETAR
    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        familiaService.deletar(id);
        return "redirect:/familias";
    }
}