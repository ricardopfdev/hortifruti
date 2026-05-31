package com.hortifruti.controller;

import com.hortifruti.service.FamiliaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FamiliaController {

    private final FamiliaService familiaService;

    public FamiliaController(FamiliaService familiaService) {
        this.familiaService = familiaService;
    }

    @GetMapping("/familias")
    public String listarFamilias(Model model) {

        model.addAttribute("familias",
                familiaService.listarTodas());

        return "pages/familias";
    }
}