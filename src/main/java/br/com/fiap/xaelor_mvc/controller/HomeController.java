package br.com.fiap.xaelor_mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Rota pública - página inicial
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
