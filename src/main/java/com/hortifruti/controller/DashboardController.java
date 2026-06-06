package com.hortifruti.controller;

import com.hortifruti.service.FamiliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private FamiliaService familiaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalFamilias", familiaService.totalFamilias());
        model.addAttribute("familiasHoje", familiaService.familiasHoje());
        model.addAttribute("familiasNaFila", familiaService.familiasNaFila());
        model.addAttribute("familiasAtendidas", familiaService.familiasAtendidas());
        model.addAttribute("filaTop5", familiaService.filaTop5());

        return "dashboard";
    }
}