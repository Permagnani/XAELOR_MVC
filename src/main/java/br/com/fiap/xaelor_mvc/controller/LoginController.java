package br.com.fiap.xaelor_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Rota pública - página de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
