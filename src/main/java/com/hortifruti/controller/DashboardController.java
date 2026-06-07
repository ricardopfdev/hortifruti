package com.hortifruti.controller;

import com.hortifruti.service.EntregaService;
import com.hortifruti.service.FamiliaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final FamiliaService familiaService;
    private final EntregaService entregaService;

    public DashboardController(FamiliaService familiaService, EntregaService entregaService) {
        this.familiaService = familiaService;
        this.entregaService = entregaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalFamilias", familiaService.totalFamilias());
        model.addAttribute("familiasHoje", familiaService.familiasHoje());
        model.addAttribute("familiasNaFila", familiaService.familiasNaFila());
        model.addAttribute("familiasAtendidas", familiaService.familiasAtendidas());
        model.addAttribute("entregasHoje", entregaService.entregasHoje());
        model.addAttribute("filaTop5", familiaService.filaTop5());
        return "dashboard";
    }
}
