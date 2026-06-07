package com.hortifruti.controller;

import com.hortifruti.service.EntregaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entregas", entregaService.listarTodas());
        model.addAttribute("entregasHoje", entregaService.entregasHoje());
        return "entregas/lista";
    }
}
