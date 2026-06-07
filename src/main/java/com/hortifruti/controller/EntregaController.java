package com.hortifruti.controller;

import com.hortifruti.model.FiltroEntrega;
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
    public String listar(FiltroEntrega filtro, Model model) {
        var pagina = entregaService.listar(filtro);
        model.addAttribute("filtro", filtro);
        model.addAttribute("pagina", pagina);
        model.addAttribute("entregas", pagina.getContent());
        model.addAttribute("entregasHoje", entregaService.entregasHoje());
        return "entregas/lista";
    }
}
