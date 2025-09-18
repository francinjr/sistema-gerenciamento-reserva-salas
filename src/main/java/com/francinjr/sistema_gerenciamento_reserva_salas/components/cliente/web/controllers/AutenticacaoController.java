package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutenticacaoController {

    @GetMapping("/login")
    public String exibirPaginaDeLogin() {
        return "autenticacao/login";
    }
}
