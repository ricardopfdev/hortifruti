package com.hortifruti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("erro",
                    "Usuário ou senha inválidos. Famílias: CPF + senha de 5 caracteres. Admin e atendentes: usuário e senha completos.");
        }
        if (logout != null) {
            model.addAttribute("sucesso", "Logout realizado com sucesso.");
        }
        return "login";
    }
}
