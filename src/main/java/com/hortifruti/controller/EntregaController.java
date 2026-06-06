package com.hortifruti.controller;

import com.hortifruti.entity.Familia;
import com.hortifruti.repository.FamiliaRepository;
import com.hortifruti.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/entrega")
public class EntregaController {

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private EntregaService entregaService;

    @GetMapping("/registrar/{id}")
    public String registrar(@PathVariable Long id) {

        Familia familia = familiaRepository.findById(id)
                .orElseThrow();

        entregaService.registrarEntrega(familia);

        return "redirect:/familias";
    }
}
